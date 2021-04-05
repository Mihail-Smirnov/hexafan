function updateRooms(rooms){
  activeRooms = rooms
  if(isRoomCodeUpdated){
    isRoomCodeUpdated = false
    updateMyRoomCode()
  }else{
    updateRoomTable()
  }
}

function getCheckedValue(elementName){
  var rad = document.getElementsByName(elementName)
  for (var i = 0; i < rad.length; i++) {
    if (rad[i].checked) {
	    return rad[i].value
    }
  }
}

function showMessage(header, message){
  messageHeader.innerHTML = header
  messageText.innerHTML = message
  messageDialog.showModal()
}

function closeMessageDialog(){
  messageDialog.close()
}

function showHasRoomMessage(){
  showMessage("Ошибка", "У вас уже есть комната")
}

function openCreateRoomDialog(){
  if(myRoomCode != null){
      return
  }
	createRoomDialog.showModal()
}

function closeCreateRoomDialog(){
	createRoomDialog.close()
}

function saveCreateRoomDialog(){
	var settings = {}
	settings.moveDuration = parseInt(getCheckedValue("movetime-radio"), 10)
	settings.firstPlayer = getCheckedValue("firstPlayer-radio")
	settings.boardSize = parseInt(getCheckedValue("boardSize-radio"), 10)
	settings.arrangement = getCheckedValue("arrangement-radio")
	var room = {settings:settings,hidden:document.getElementById("hiddenRoom").checked}
	sendRoom(room)
	createRoomDialog.close()
}

function sendRoom(room){
  isRoomCodeUpdated = true
  stompClient.send("/app/waiting/createRoom", {}, JSON.stringify(room))
}

function deleteRoom(){
  if(myRoomCode != null){
    isRoomCodeUpdated = true
    stompClient.send("/app/waiting/deleteRoom", {}, {})
  }
}

function openSearchRoomDialog(){
  if(myRoomCode != null){
      showHasRoomMessage()
      return
  }
	searchRoomDialog.showModal()
}

function closeSearchRoomDialog(){
	searchRoomDialog.close()
}

function saveSearchRoomDialog(){
  if(myRoomCode != null){
      showHasRoomMessage()
      return
  }

  var code = roomCodeTb.value
	sendRoomCode(code)
	searchRoomDialog.close()
}

function connectToRoom(code, owner){
  if(!owner && code === myRoomCode){
    showHasRoomMessage()
    return
  }

  if(myRoomConnection != null){
    stompClient.unsubscribe(myRoomConnection.id)
    myRoomConnection = null
  }
  myRoomConnection = stompClient.subscribe("/waiting/room/" + code, function(response) {
    if(response.body) {
      startGame(response.body)
    }else{
      showMessage("Ошибка", "Невозможно подключиться к комнате")
      if(myRoomConnection != null){
        stompClient.unsubscribe(myRoomConnection.id)
        myRoomConnection = null
      }
    }
  })
}

function sendRoomCode(code){
  connectToRoom(code)
  stompClient.send("/app/waiting/connectRoom/" + code, {}, code)
}

function connectWaiting(){
  var socket = new SockJS('/waiting-messaging');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/waiting/activeRooms', function(response) {
		  if(response) updateRooms(JSON.parse(response.body))
		})
		getActiveRooms()
	})
}

function disconnectWaiting(){
	stompClient.disconnect();
}

function getActiveRooms(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/activeRooms')
  xhr.onload = () => {
    if(xhr.response) updateRooms(JSON.parse(xhr.response))
  }
  xhr.send()
}

function startGame(gameId){
  location.replace("/game/" + gameId)
}

function updateMyRoomCode(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/myRoomCode')
  xhr.onload = () => {
    if(xhr.response){
      myRoomCode = xhr.response
      fastGameBtn.disabled = true
      openCreateRoomBtn.disabled = true
      openSearchRoomBtn.disabled = true
      deleteRoomBtn.hidden = false
      myRoomCodeTxt.style.visibility = "visible"
      myRoomCodeTxt.innerHTML = "Код вашей комнаты: " + myRoomCode
      if(myRoomConnection == null){
        connectToRoom(myRoomCode, true)
      }
    }else{
      myRoomCode = null
      fastGameBtn.disabled = false
      openCreateRoomBtn.disabled = false
      openSearchRoomBtn.disabled = false
      deleteRoomBtn.hidden = true
      myRoomCodeTxt.style.visibility = "hidden"
      if(myRoomConnection != null){
        stompClient.unsubscribe(myRoomConnection.id)
        myRoomConnection = null
      }
    }
    updateRoomTable()
  }
  xhr.send()
}

function fastGame(){
  if(myRoomCode != null){
    showHasRoomMessage()
    return
  }

  var xhr = new XMLHttpRequest()
  xhr.open('POST', '/data/fastGame')
  xhr.onload = () => {
    if(xhr.response){
      sendRoomCode(xhr.response)
    }else{
      sendRoom(makeDefaultRoom())
    }
  }
  xhr.send()
}

function makeDefaultRoom(){
  return {settings:{moveDuration:60, firstPlayer:"WHITE", boardSize:6, arrangement:"NORMAL"},hidden:false}
}

function getRoomButtonHTML(code){
  if(myRoomCode == null){
    return '<button class="btn" onclick="sendRoomCode('+"'"+code+"'"+')">Начать</button>'
  }else{
    if(myRoomCode === code){
      return '<button class="btn-red" onclick="deleteRoom()">Удалить</button>'
    }else{
      return '<button class="btn" disabled>Начать</button>'
    }
  }
}

function getRoomRowHTML(room, code){
  var trClass = code === myRoomCode ? "class='highlighted'" : ""
  return "<tr><td "+trClass+">"+room.owner
    +"</td><td "+trClass+">"+durations[room.settings.moveDuration]
    +"</td><td "+trClass+">"+players[room.settings.firstPlayer]
    +"</td><td "+trClass+">"+room.settings.boardSize
    +"</td><td "+trClass+">"+arrangements[room.settings.arrangement]
    +"</td><td "+trClass+">"+getRoomButtonHTML(code)+"</td></tr>"
}

function updateRoomTable(){
  if(Object.keys(activeRooms).length > 0){
    roomTable.innerHTML = ""
    for(var code in activeRooms){
      roomTable.innerHTML += getRoomRowHTML(activeRooms[code], code)
    }
  }else{
    roomTable.innerHTML = '<tr><td style="text-align: center;" colspan="6">Комнат не найдено</td></tr>'
  }
}

var roomTable = document.getElementById("room-table-body")

var createRoomDialog = document.getElementById("create-room-dialog")
var openCreateRoomBtn = document.getElementById("open-create-room-btn")
openCreateRoomBtn.addEventListener("click", openCreateRoomDialog)
var closeCreateRoomBtn = document.getElementById("close-create-room-btn")
closeCreateRoomBtn.addEventListener("click", closeCreateRoomDialog)
var saveCreateRoomBtn = document.getElementById("save-create-room-btn")
saveCreateRoomBtn.addEventListener("click", saveCreateRoomDialog)

var fastGameBtn = document.getElementById("fast-game-btn")
fastGameBtn.addEventListener("click", fastGame)
var deleteRoomBtn = document.getElementById("delete-room-btn")
deleteRoomBtn.addEventListener("click", deleteRoom)
var roomCodeTb = document.getElementById("room-code-tb")

var searchRoomDialog = document.getElementById("search-room-dialog")
var openSearchRoomBtn = document.getElementById("open-search-room-btn")
openSearchRoomBtn.addEventListener("click", openSearchRoomDialog)
var closeSearchRoomBtn = document.getElementById("close-search-room-btn")
closeSearchRoomBtn.addEventListener("click", closeSearchRoomDialog)
var saveSearchRoomBtn = document.getElementById("save-search-room-btn")
saveSearchRoomBtn.addEventListener("click", saveSearchRoomDialog)

var messageDialog = document.getElementById("message-dialog")
var messageHeader = document.getElementById("message-dialog-header")
var messageText = document.getElementById("message-dialog-text")
var closeMessageBtn = document.getElementById("close-message-btn")
closeMessageBtn.addEventListener("click", closeMessageDialog)

var myRoomCode = null
var myRoomCodeTxt = document.getElementById("my-room-code-txt")
var myRoomConnection = null
var isRoomCodeUpdated = true
var activeRooms = null

var durations = {30:"30 сек.", 60:"1 мин.", 120:"2 мин.", 300:"5 мин."}
var players = {WHITE:"Белые", BLACK:"Черные"}
var arrangements = {NORMAL:"Обычная", WIDE:"Широкая"}

connectWaiting()