//CS 427 T25 
function generateNewID (oldID) {
  var newID;
  do{
    newID = parseInt(Math.random()*100);
  }while(newID==oldID);
  return newID;
}
QUnit.test( "Test On Initialization Success", function( assert ) {
  assert.equal( me.buddyList.length, 0, "Initialization succeeded");
});

QUnit.test( "Test On User Connected Success", function( assert ) {
  var old_len = me.buddyList.length;
  var id = parseInt(Math.random()*100);
  onUserConnect(id, id);
  assert.equal( me.buddyList.length, old_len+1, "1st new user has been added to buddy list" );
  assert.equal( me.buddyList[old_len].name, id, "1st new user has the requested name");
  old_len = me.buddyList.length;
  id = generateNewID(id);
  onUserConnect(id, id);
  assert.equal( me.buddyList.length, old_len+1, "2nd new user has been added to buddy list" );
  assert.equal( me.buddyList[old_len].name, id, "2nd new user has the requested name");
});

QUnit.test( "Test On Existed User Connected", function (assert) {
  var old_len = me.buddyList.length;
  var id = parseInt(Math.random()*100);
  onUserConnect(id, id);
  assert.equal( me.buddyList.length, old_len+1, "3rd new user has been added to buddy list" );
  assert.equal( me.buddyList[old_len].name, id, "3rd new user has the requested name");
  old_len = me.buddyList.length;
  onUserConnect(id, id);
  assert.equal( me.buddyList.length, old_len, "No user with duplicated id/name will be added" );
});

QUnit.test( "Test On User Disconnected", function( assert ) {
  var old_len = me.buddyList.length;
  var i = parseInt(Math.random()*100)%old_len;
  var id = me.buddyList[i].id;
  onUserDisconnect("", id);
  assert.equal( me.buddyList.length, old_len, "One Random User has been Disconnected" );
  assert.equal( me.buddyList[i].online, false, "This user is no more online");
});

QUnit.test( "Test On User Disconnected then Connected Again", function (assert) {
  var old_len = me.buddyList.length;
  var i = parseInt(Math.random()*70+Math.random()*30)%old_len;
  var id = me.buddyList[i].id;
  onUserDisconnect("", id);
  assert.equal( me.buddyList.length, old_len, "One Random User has been Disconnected" );
  assert.equal( me.buddyList[i].online, false, "This user is no more online");
  onUserConnect(id, id);
  assert.equal( me.buddyList.length, old_len, "No user with duplicated id/name will be added" );
  assert.equal( me.buddyList[i].online, true, "This user is back online");
});
