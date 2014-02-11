var socket = null;
var socketIO = {
	hostname: "localhost",
	port: 1214,
};

var config = {
	chat_selector: "#chatDiv",
	list_selector: "#buddyListDiv",
	box_width: 260,
	box_offset: 280
};

var me = {
	name: "me",
	buddyList: [],
	talkingList: [],//idx
	AddNewBuddy: function (name, id) {
		for (var i=0; i < this.buddyList.length; i++) {
			if (this.buddyList[i].id==id) {
				return false;
			};
		};
		var p = new Person(name, id);
		this.buddyList.push(p);
		return true;
	},
	BeginTalkingTo: function (p) {
		var idx = this.buddyList.indexOf(p);
		if (this.talkingList.indexOf(idx)==-1) {
			this.talkingList.push(idx);
			p.BeginTalking();
		};
	},
	EndTalkingTo: function (p) {
		var idx = this.buddyList.indexOf(p);
		var idx2 = this.talkingList.indexOf(idx);
		this.talkingList.splice(idx2, 1);
		//console.log(this.talkingList);
		this.AdjustLayout(p);
	},
	AdjustLayout: function (p) {
		for (var i = 0; i < this.talkingList.length; i++) {
			$("#"+this.buddyList[this.talkingList[i]].box_id).chatbox("option", "offset", config.box_offset*i);
		}
	},
	onBuddyListClick: function (id){
		for (var i=0; i < this.buddyList.length; i++) {
			if (this.buddyList[i].id==id) {
				return this.BeginTalkingTo(this.buddyList[i]);
			};
		};
	},
	onBuddyOnlineStatusChange: function (id, online) {
		for (var i=0; i < this.buddyList.length; i++) {
			if (this.buddyList[i].id==id) {
				if (this.buddyList[i].online != online) {
					this.buddyList[i].online = online;
					//"<span class="+(online?"'online-box'":"'offline-box'")+">"+this.buddyList[id].name+"</span>"
					$("#"+this.buddyList[i].box_id).chatbox("option", "titleclass", online?"online-box":"offline-box");
					friends.Refresh(id, online);
				};
				break;
			};
		};

	},
	onMessageReceive: function (sender, msg) {
		// when message received
		var divid = ["box_",this.name,"_",sender].join('');
		$("#"+divid).chatbox("option", "boxManager").addMsg(sender, appendTimestamp(msg));
		if (isSoundEnabled()) {
			playSound();
		};
	},
	onMessageSent: function (divid, sender, msg) {
		// when message send
		$("#"+divid).chatbox("option", "boxManager").addMsg(sender, appendTimestamp(msg));
		// if (isSoundEnabled()) {
		// 	playSound();
		// };
	}
};

function Person (name, id) {
	this.name = name;
	this.id = id;
	this.online = false;
	this.talking = false;
	this.box = null;
	this.box_id = "";
}

Person.prototype.BeginTalking = function() {
	if (this.talking) return console.log(this.name+" is already in conversation with you.");
	console.log("Conversation with "+this.name+" begins.");
	this.talking = true;

	/*Create the chat box here*/
	this.box_id = ["box_",me.name,"_",this.name].join('');
	$(config.chat_selector).append("<div id='"+this.box_id+"'></div>");
	this.box = $("#"+this.box_id).chatbox({
		id: this.box_id,
		user: me.name,
		title: this.name,
		titleclass: this.online?"online-box":"offline-box",
		person: this,
		offset: (me.talkingList.length-1)*config.box_offset,
		width: config.box_width,
		messageSent: function (boxid, user, msg , person) {
			me.onMessageSent(boxid, "me", msg);
			var jsonObject = 
			{
				'@class': 'edu.illinois.t25.net.ChatMessage',
				author: user,
				contents: msg,
				group : person.name
			};
			socket.json.send(jsonObject);
		},
		boxClosed: function (boxid, person) {
			me.EndTalkingTo(person);
			person.EndTalking();
			$("div[_id="+boxid+"]").remove();//remove div
		}
	});
	/*Finish the chat box creation*/
};

Person.prototype.EndTalking = function() {
	this.talking = false;
	this.box = null;
	this.box_id = "";
};

var friends = {
	list: [],
	CreateView: function () {
		var ret = "";
		// for (var i = 0; i < this.list.length; i++) {
		// 	ret += "<li id='buddy"+this.list[i].id+"'><a onclick='me.onBuddyListClick("+this.list[i].id+")'><strong><span class='offline-box'>"+this.list[i].name+"</span></strong><span class='pull-right'>offline</span></a></li>";
		// };
		$(config.list_selector).html("<ul id='onlinelist'></ul>");
	},
	AddItem: function (_id, _name) {
		for (var i = 0; i < this.list.length; i++) {
			if (this.list[i].id==_id) {
				return false;
			};
		};
		this.list.push({id: _id, name: _name});
		var ret = "<li id='buddy"+_id+"'><a onclick='me.onBuddyListClick(\""+_id+"\") '><strong><span class='offline-box'>"+_name+"</span></strong><span class='pull-right'>offline</span></a></li>";
		$("ul#onlinelist").append(ret);
		return true;
	},
	Refresh: function (id, online) {
		var name = "";
		for (var i = 0; i < this.list.length; i++) {
			if (this.list[i].id==id) {
				name = this.list[i].name;
				break;
			};
		};
		var _html = "<a onclick='me.onBuddyListClick(\""+id+"\") '><strong><span class="+(online?"'online-box'":"'offline-box'")+">"+name+"</span></strong><span class='pull-right'>"+(online?"online":"offline")+"</span></a></li>";
		$("li#buddy"+id).html(_html);
	}
};

function onUserConnect(name, id) {
	var isNew = me.AddNewBuddy(name, id);
	if (isNew) {
		friends.AddItem(id, name);
	};
	me.onBuddyOnlineStatusChange(id, true);
	return isNew;
}

function onUserDisconnect(name, id) {
	me.onBuddyOnlineStatusChange(id, false);
}

function onInit(){
	//var namelist = ["Joe", "Vikram", "Ke", "Piyush", "Qiyue", "Suk", "Tim", "Zhuoyuan"];
	me.buddyList = [];
	friends.list = [];
	friends.CreateView();
	socket = io.connect("http://"+socketIO.hostname+":"+socketIO.port.toString());
	//me.name = "";//name me here
	socket.on('connect', function() {
		console.log("Client has connected to the server!");
	});

	socket.on('connection-change', function (data) {
		if (data.connectorIsConnecting) {
			onUserConnect(data.connector, data.connector);
		}else{//disconnect
			onUserDisconnect(data.connector,data.connector);
		} 
		console.log("connection-change::"+data);
	});
	
	socket.on('message', function(data) {
		me.onMessageReceive(data.author, data.contents);
	});
	
	socket.on('disconnect', function() {
		console.log("The client has disconnected!")
	});
}

function playSound () {
	$("audio#player").remove();
	$("<audio id='player' autoplay='autoplay' style='display:none;' controls='controls'><source src='msg.ogg' /></audio>").appendTo('body');
}

function appendTimestamp (msg) {
	var d = new Date();
	var dstr = [d.getHours(), d.getMinutes(), d.getSeconds()].join(":");
	return msg+"["+dstr+"]";
}

function isSoundEnabled () {
	return ($("#sound_switch").attr('checked')==="checked");
}

/* MAIN function :-) */
$(function(){
	$('#sound_switch').tzCheckbox({labels:['Enable','Disable']});
	onInit();
});

