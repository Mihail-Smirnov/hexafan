function clickOnCell(e){
	var cellName = getClickedCell(e)
	if(cellName === null || gameStatus.finished || gameStatus.activePlayer !== player.color) return
	var figure = cells[cellName].figure
	if(figure !== null && figure.player == player.color){
		player.selectedFigure = figure
		markMoveAndAttackCells(figure)
	}else if(cells[cellName].underMove){
	  doMove({from:player.selectedFigure.cell,to:cellName})
	  player.selectedFigure = null
	  clearMark()
	  drawBoard()
	}
}

function loadGame(game){
  updateSettings(game.settings)
  player.color = game.myColor
  initCells()
  cells = game.cells
  updateStatus(game.status)

  chat.innerHTML = ""
  addMessagesToChat(game.messages)
  moveTimer = setInterval(updateMoveTime, 1000);
}

function tryLoadGame(){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/'+gameId+'/game')
  xhr.onload = () => {
    loadGame(JSON.parse(xhr.response))
  }
  xhr.send()
}

function checkGameStatus(callback){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/'+gameId+'/status' + location.search)
  xhr.onload = () => {
    callback(JSON.parse(xhr.response))
  }
  xhr.onerror = () => {
    moveTime = -1
    messageDialogType = "redirect"
    showMessage("Ошибка", "Игра не найдена")
  }
  xhr.send()
}

function updateStatus(status){
  if(status.started){
    gameStatus = status

    if(gameStatus.activePlayer === "WHITE"){
      whitePlayerUsername.style.color = "blue"
      blackPlayerUsername.style.color = "black"
    }else if(gameStatus.activePlayer === "BLACK"){
      whitePlayerUsername.style.color = "black"
      blackPlayerUsername.style.color = "blue"
    }

    var time = (Date.parse(gameStatus.currentTime)-Date.parse(gameStatus.lastMoveTime))/1000
    moveTime = Math.max(0, Math.floor(gameSettings.moveDuration - time))

    updateFinalMessage(gameStatus.winner)
    if(gameStatus.figures != null) addFiguresToBoard(gameStatus.figures)
  }
}

function updateMoveTime(){
  if(!gameStatus.finished && moveTime == 0){
    checkGameStatus((status) => updateStatus(status))
    return
  }
  if(gameStatus.finished) moveTime = 0;
  if(moveTime >= 0){
    var minutes = moveTime/60
    timerMinutesFirst.innerHTML = Math.floor(minutes/10)
    timerMinutesLast.innerHTML = Math.floor(minutes%10)
    var seconds = moveTime%60
    timerSecondsFirst.innerHTML = Math.floor(seconds/10)
    timerSecondsLast.innerHTML = Math.floor(seconds%10)
    moveTime--
  }
}

function showMessage(header, message){
  messageHeader.innerHTML = header
  messageText.innerHTML = message
  messageDialog.showModal()
}

function closeMessageDialog(){
  messageDialog.close()
  if(messageDialogType === "redirect") location.replace("/")
}

function showQuestion(header, message, ok, reject){
  okQuestionCallback = ok
  rejectQuestionCallback = reject
  questionHeader.innerHTML = header
  questionText.innerHTML = message
  questionDialog.showModal()
}

function okQuestionDialog(){
  okQuestionCallback()
  questionDialog.close()
}

function rejectQuestionDialog(){
  rejectQuestionCallback()
  questionDialog.close()
}

function showSystemMessage(message){
  if(message == null) return
  if(message.statusUpdated){
    checkGameStatus((status) => updateStatus(status))
  }
  if(message.sender !== player.color){
    if(message.type === "LOSE"){
      showMessage("Победа", "Ваш противник сдался")
    }else if(message.type === "DRAW"){
      showQuestion("Ничья", "Противник предлагает вам ничью. Вы согласны?", sendOKDrawMessage, sendRejectDrawMessage)
    } else if(message.type === "DRAW_OK"){
      showMessage("Ничья", "Ваш противник согласился на ничью")
    }else if(message.type === "DRAW_FAIL"){
      showMessage("Ничья", "Ваш противник отказался от ничьи")
    }
  }
}

function addMessageToChat(message){
  if(message.sender == null){
    messageDialogType = "redirect"
    showMessage("Ошибка", "Игра не найдена")
  }else{
    chat.innerHTML += "<div class='chat__message'><p><b>"+message.sender+":</b> "+message.text+"</p></div>"
  }
}

function addMessagesToChat(messages){
  for(var i = 0; i < messages.length; i++){
    addMessageToChat(messages[i])
  }
}

function connect() {
	var socket = new SockJS('/game-messaging')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, function(frame) {
	    subscribeAll()
	})
}

function subscribeAll(){
	stompClient.subscribe('/game/'+gameId+'/status', function(response) {
    updateStatus(JSON.parse(response.body))
  })
	stompClient.subscribe('/game/'+gameId+'/systemMessages', function(response) {
    showSystemMessage(JSON.parse(response.body))
  })
	stompClient.subscribe('/game/'+gameId+'/messages', function(response) {
    addMessageToChat(JSON.parse(response.body))
  })
}

function doMove(move){
	stompClient.send("/app/game/"+gameId+"/move", {}, JSON.stringify({moveVm:move,gameId:gameId}));
}

function sendMessage(text){
  stompClient.send("/app/game/"+gameId+"/message", {}, JSON.stringify({text:text,gameId:gameId}));
}

function sendMessageFromTextBox(){
  if(chatTextBox.value !== ''){
    sendMessage(chatTextBox.value)
    chatTextBox.value=''
  }
}

function sendSystemMessage(message){
	stompClient.send("/app/game/"+gameId+"/systemMessage", {}, JSON.stringify(message));
}

function sendDrawOffer(){
  sendSystemMessage({type:"DRAW", sender:player.color, gameId:gameId, statusUpdated:false})
}

function sendLoseMessage(){
  sendSystemMessage({type:"LOSE", sender:player.color, gameId:gameId, statusUpdated:true})
}

function sendRejectDrawMessage(){
  sendSystemMessage({type:"DRAW_FAIL", sender:player.color, gameId:gameId, statusUpdated:false})
}

function sendOKDrawMessage(){
  sendSystemMessage({type:"DRAW_OK", sender:player.color, gameId:gameId, statusUpdated:true})
}

function disconnect(){
	stompClient.disconnect();
}

var messageDialog = document.getElementById("message-dialog")
var messageHeader = document.getElementById("message-dialog-header")
var messageText = document.getElementById("message-dialog-text")
var closeMessageBtn = document.getElementById("close-message-btn")
closeMessageBtn.addEventListener("click", closeMessageDialog)
var messageDialogType = null

var questionDialog = document.getElementById("question-dialog")
var questionHeader = document.getElementById("question-dialog-header")
var questionText = document.getElementById("question-dialog-text")
var okMessageBtn = document.getElementById("ok-question-btn")
okMessageBtn.addEventListener("click", okQuestionDialog)
var rejectQuestionBtn = document.getElementById("reject-question-btn")
rejectQuestionBtn.addEventListener("click", rejectQuestionDialog)
var okQuestionCallback = null
var rejectQuestionCallback = null

var path = location.pathname.split('/')
var gameId = path[path.length-1]
var player = {color:'WHITE', selectedFigure:null}

canvas.addEventListener('click', clickOnCell)
var chat = document.getElementById('messages')
var chatTextBox = document.getElementById('message-text')
var whitePlayerUsername = document.getElementById("white-player-username")
var blackPlayerUsername = document.getElementById("black-player-username")
var timerMinutesFirst = document.getElementById("timer-minutes-first")
var timerMinutesLast = document.getElementById("timer-minutes-last")
var timerSecondsFirst = document.getElementById("timer-seconds-first")
var timerSecondsLast = document.getElementById("timer-seconds-last")
var moveTime = 0
var moveTimer = null

checkGameStatus((status) => {
  gameStatus = status
  if(status.started){
    loadUserSettings(tryLoadGame)
    connect()
  }else{
    messageDialogType = "redirect"
    showMessage("Ошибка", "Игра не найдена")
  }
})