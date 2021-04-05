function getCellColors(){
  if(userSettings.boardStyle === 'COLORFUL'){
    return ['green', 'yellow', 'red']
  }else if(userSettings.boardStyle === 'BEIGE'){
    return ['rgb(169,87,27)', 'rgb(191,132,75)', 'rgb(214,177,124)']
  }else{
    return null
  }
}

function getFigureDrawFactory(){
  if(userSettings.figureStyle === 'GEOMETRY'){
    return geometryFigureDrawFactory
  }else if(userSettings.figureStyle === 'CLASSIC'){
    return classicFigureDrawFactory
  }else{
    return null
  }
}

function getCellCenter(cell){
	var center = {}
	center.x = boardView.scale*(3/4*cell.pos.x+1/2)+boardView.padding.x
	center.y = boardView.scale*(Math.sqrt(3)/4*cell.pos.y+1/2)+boardView.padding.y
	return center
}

function drawHexagon(pos, size){
	var k = Math.sqrt(3)/4
	ctx.beginPath()
	ctx.moveTo(pos.x+size/2, pos.y)
	ctx.lineTo(pos.x+size/4, pos.y+size*k)
	ctx.lineTo(pos.x-size/4, pos.y+size*k)
	ctx.lineTo(pos.x-size/2, pos.y)
	ctx.lineTo(pos.x-size/4, pos.y-size*k)
	ctx.lineTo(pos.x+size/4, pos.y-size*k)
	ctx.lineTo(pos.x+size/2, pos.y)
	ctx.fill()
	ctx.stroke()
}

function insideCell(cell, x, y){
	var pos = getCellCenter(cell)
	var r = boardView.scale/2
	var x0 = x-pos.x
	var y0 = y-pos.y
	var r0 = x0**2 + y0**2
	if(r0 > r**2) return false
	if(r0 < r**2*3/4) return true

	var py = y0*2/Math.sqrt(3)
	var px = py/2+x0
	return Math.abs(px) <= r && Math.abs(py) <= r && Math.abs(px-py) <= r
}

function drawMovingCell(cell){
	ctx.lineWidth = boardView.scale/25*2
	ctx.strokeStyle = 'blue'
	ctx.fillStyle = 'rgba(0,0,255,0.2)'
	drawHexagon(getCellCenter(cell), boardView.scale)
}

function drawAttackingCell(cell){
	ctx.lineWidth = boardView.scale/25*2
	ctx.strokeStyle = 'red'
	ctx.fillStyle = 'rgba(255,0,0,0.2)'
	drawHexagon(getCellCenter(cell), boardView.scale)
}

function drawCell(cell) {
	drawHexagon(getCellCenter(cell), boardView.scale)
}

function drawCellDetails(cell){
	if(cell.underMove) drawMovingCell(cell)
	if(cell.figure != null) cell.figure.draw()
	if(finalMessage != null) drawCellShadow(cell)
}

function drawCellShadow(cell){
	ctx.save()
	ctx.fillStyle = "rgba(0,0,0,0.5)"
	ctx.strokeStyle = "rgba(0,0,0,0.5)"
	drawHexagon(getCellCenter(cell), boardView.scale)
  ctx.restore()
}

var geometryFigureDrawFactory = {
  drawPawn: function(){
    this.setColor()
	  var pos = getCellCenter(cells[this.cell])
	  ctx.beginPath()
	  ctx.arc(pos.x, pos.y, boardView.scale/8, 0, Math.PI*2)
  	ctx.fill()
	  ctx.stroke()
  },
  drawKing: function(){
    this.setColor();
	  var pos = getCellCenter(cells[this.cell])
	  ctx.beginPath()
	  ctx.arc(pos.x, pos.y, boardView.scale/4, 0, Math.PI*2)
	  ctx.fill()
	  ctx.stroke()
  },
  drawRook: function(){
    this.setColor();
	  drawHexagon(getCellCenter(cells[this.cell]), boardView.scale/2)
  },
  drawKnight: function(){
    this.setColor();
	  var pos = getCellCenter(cells[this.cell])
	  ctx.save()
	  ctx.lineWidth = boardView.scale/8
	  ctx.strokeStyle = ctx.fillStyle
	  ctx.beginPath()
	  ctx.arc(pos.x, pos.y, boardView.scale*3/16, 0, Math.PI*2)
	  ctx.stroke()
	  ctx.restore()
	  ctx.beginPath()
	  ctx.arc(pos.x, pos.y, boardView.scale/8, 0, Math.PI*2)
	  ctx.stroke()
	  ctx.beginPath()
	  ctx.arc(pos.x, pos.y, boardView.scale/4, 0, Math.PI*2)
	  ctx.stroke()
  },
  drawBishop: function(){
    this.setColor();
	  var pos = getCellCenter(cells[this.cell])
	  var rotation = this.player === 'WHITE' ? 'TOP':'BOTTOM'
	  var k = Math.sqrt(3)/8
	  ctx.beginPath()
	  if(rotation === 'TOP'){
		  ctx.moveTo(pos.x, pos.y-boardView.scale/4)
		  ctx.lineTo(pos.x-boardView.scale*k, pos.y+boardView.scale/8)
		  ctx.lineTo(pos.x+boardView.scale*k, pos.y+boardView.scale/8)
		  ctx.lineTo(pos.x, pos.y-boardView.scale/4)
	  }else if(rotation === 'BOTTOM'){
  		ctx.moveTo(pos.x, pos.y+boardView.scale/4)
	  	ctx.lineTo(pos.x-boardView.scale*k, pos.y-boardView.scale/8)
		  ctx.lineTo(pos.x+boardView.scale*k, pos.y-boardView.scale/8)
		  ctx.lineTo(pos.x, pos.y+boardView.scale/4)
	  }
	  ctx.fill()
	  ctx.stroke()
  },
  drawQueen: function(){
    this.setColor();
	  var pos = getCellCenter(cells[this.cell])
  	var k = Math.sqrt(3)/8
	  ctx.beginPath()
	  ctx.moveTo(pos.x, pos.y-boardView.scale/4)
	  ctx.lineTo(pos.x-boardView.scale*k, pos.y+boardView.scale/8)
	  ctx.lineTo(pos.x+boardView.scale*k, pos.y+boardView.scale/8)
	  ctx.lineTo(pos.x, pos.y-boardView.scale/4)
	  ctx.fill()
	  ctx.stroke()
	  ctx.beginPath()
	  ctx.moveTo(pos.x, pos.y+boardView.scale/4)
  	ctx.lineTo(pos.x-boardView.scale*k, pos.y-boardView.scale/8)
    ctx.lineTo(pos.x+boardView.scale*k, pos.y-boardView.scale/8)
  	ctx.lineTo(pos.x, pos.y+boardView.scale/4)
	  ctx.fill()
	  ctx.stroke()
  }
}

function drawFigureImage(figure){
	var pos = getCellCenter(cells[figure.cell])
	var size = boardView.scale*2/3
	var figureId = figure.type+figure.player

	if(!imagePool[figureId]){
	  loadingImage++
	  var image = new Image();
    image.src = "/img/figures/"+figureId+".png"
    image.onload = () => {
      ctx.drawImage(image, pos.x-size/2, pos.y-size/2, size, size)
      loadingImage--
      if(loadingImage == 0) drawBoard()
      imagePool[figureId] = image
    }
	}else{
	  var image = imagePool[figureId]
	  ctx.drawImage(image, pos.x-size/2, pos.y-size/2, size, size)
	}
}

var classicFigureDrawFactory = {
  drawPawn: function(){drawFigureImage(this)},
  drawKing: function(){drawFigureImage(this)},
  drawRook: function(){drawFigureImage(this)},
  drawKnight: function(){drawFigureImage(this)},
  drawBishop: function(){drawFigureImage(this)},
  drawQueen: function(){drawFigureImage(this)}
}

function checkObstacle(cell){
	return cell === '' || cells[cell].figure !== null
}

function checkUnderAttack(cell, figure){
	return cell !== '' && cells[cell].figure !== null && cells[cell].figure.player !== figure.player
}

function addNearCell(cell, figure, nearCells){
	if(checkUnderAttack(cell,figure)) nearCells.push({name:cell,type:'ATTACK'})
	else if(!checkObstacle(cell)) nearCells.push({name:cell,type:'MOVE'})
}

function getCellByPath(cell, path){
	for(var i = 0; i < path.length; i++){
		if(cell === '') return cell
		cell = cells[cell].sides[path[i]]
	}
	return cell
}

function makeFigureBase(cellName, playerColor){
	var figure = {}
	figure.cell = cellName
	figure.player = playerColor
	if(figure.player === 'WHITE') figure.setColor = () => {ctx.fillStyle = 'white';ctx.strokeStyle = 'black';ctx.lineWidth = boardView.scale/25}
	else figure.setColor = () => {ctx.fillStyle = 'black';ctx.strokeStyle = 'white';ctx.lineWidth = boardView.scale/25}
  return figure
}

function makePawn(cellName, playerColor, firstMove, figureDrawFactory){
	var pawn = makeFigureBase(cellName, playerColor)
	pawn.type = 'pawn'
	pawn.firstMove = firstMove
	pawn.draw = figureDrawFactory.drawPawn.bind(pawn)
	pawn.getNearCells = function(){
    var curCell = cells[this.cell]
	  var nearCells = []
	  var rot = this.player === 'WHITE' ? 0 : 3
  	if(!checkObstacle(curCell.sides[1+rot])){
  		nearCells.push({name:curCell.sides[1+rot],type:'MOVE'})
		  var doubleForwardCell = cells[curCell.sides[1+rot]].sides[1+rot]
		  if(this.firstMove && !checkObstacle(doubleForwardCell)) nearCells.push({name:doubleForwardCell,type:'MOVE'})
	  }
	  if(checkUnderAttack(curCell.diags[1+rot],this)) nearCells.push({name:curCell.diags[1+rot],type:'ATTACK'})
	  if(checkUnderAttack(curCell.diags[2+rot],this)) nearCells.push({name:curCell.diags[2+rot],type:'ATTACK'})
	  return nearCells
	}
	return pawn
}

function makeKing(cellName, playerColor, figureDrawFactory){
	var king = makeFigureBase(cellName, playerColor)
	king.type = 'king'
	king.draw = figureDrawFactory.drawKing.bind(king)
	king.getNearCells = function(){
		var curCell = cells[this.cell]
		var nearCells = []
		for(var i = 0;i < 6;i++){
			addNearCell(curCell.sides[i],this,nearCells)
			addNearCell(curCell.diags[i],this,nearCells)
		}
		return nearCells
	}
	return king
}

function makeRook(cellName, playerColor, figureDrawFactory){
	var rook = makeFigureBase(cellName, playerColor)
	rook.type = 'rook'
	rook.draw = figureDrawFactory.drawRook.bind(rook)
	rook.getNearCells = function(){
		var nearCells = []
		for(var i = 0;i < 6;i++){
			var curCell = cells[this.cell].sides[i]
			while(!checkObstacle(curCell)){
				addNearCell(curCell,this,nearCells)
				curCell = cells[curCell].sides[i]
  		}
			addNearCell(curCell,this,nearCells)
		}
		return nearCells
	}
	return rook
}

function makeKnight(cellName, playerColor, figureDrawFactory){
	var knight = makeFigureBase(cellName, playerColor)
	knight.type = 'knight'
	knight.draw = figureDrawFactory.drawKnight.bind(knight)
	knight.getNearCells = function(){
		var nearCells = []
		addNearCell(getCellByPath(this.cell,[0,0,5]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[0,0,1]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[1,1,0]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[1,1,2]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[2,2,1]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[2,2,3]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[3,3,2]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[3,3,4]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[4,4,3]),this,nearCells)
  	addNearCell(getCellByPath(this.cell,[4,4,5]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[5,5,4]),this,nearCells)
		addNearCell(getCellByPath(this.cell,[5,5,0]),this,nearCells)
		return nearCells
	}
	return knight
}

function makeBishop(cellName, playerColor, figureDrawFactory){
	var bishop = makeFigureBase(cellName, playerColor)
	bishop.type = 'bishop'
	bishop.draw = figureDrawFactory.drawBishop.bind(bishop)
	bishop.getNearCells = function(){
		var nearCells = []
		for(var i = 0;i < 6;i++){
			var curCell = cells[this.cell].diags[i]
			while(!checkObstacle(curCell)){
				addNearCell(curCell,this,nearCells)
				curCell = cells[curCell].diags[i]
			}
			addNearCell(curCell,this,nearCells)
		}
		return nearCells
	}
	return bishop
}

function makeQueen(cellName, playerColor, figureDrawFactory){
	var queen = makeFigureBase(cellName, playerColor)
	queen.type = 'queen'
	queen.draw = figureDrawFactory.drawQueen.bind(queen)
	queen.getNearCells = function(){
		var nearCells = []
		for(var i = 0;i < 6;i++){
			var curCell = cells[this.cell].sides[i]
			while(!checkObstacle(curCell)){
				addNearCell(curCell,this,nearCells)
				curCell = cells[curCell].sides[i]
			}
			addNearCell(curCell,this,nearCells)
			curCell = cells[this.cell].diags[i]
			while(!checkObstacle(curCell)){
				addNearCell(curCell,this,nearCells)
				curCell = cells[curCell].diags[i]
			}
			addNearCell(curCell,this,nearCells)
		}
		return nearCells
	}
	return queen
}

function makeFigure(figure, figureDrawFactory){
	if(figure.type === 'pawn') return makePawn(figure.cell, figure.player, figure.firstMove, figureDrawFactory)
	else if(figure.type === 'king') return makeKing(figure.cell, figure.player, figureDrawFactory)
	else if(figure.type === 'rook') return makeRook(figure.cell, figure.player, figureDrawFactory)
	else if(figure.type === 'knight') return makeKnight(figure.cell, figure.player, figureDrawFactory)
	else if(figure.type === 'bishop') return makeBishop(figure.cell, figure.player, figureDrawFactory)
	else if(figure.type === 'queen') return makeQueen(figure.cell, figure.player, figureDrawFactory)
	else return null
}

function findKing(player){
  for(var key in cells){
    if(cells[key].figure != null && cells[key].figure.type === "king" && cells[key].figure.player === player){
      return key
    }
  }
}

function drawCheckWarning(player){
  var kingCell = findKing(player)
  ctx.lineWidth = boardView.scale/25*2
  ctx.strokeStyle = 'rgb(255,0,0)'
  ctx.fillStyle = 'rgba(255,0,0,0.5)'
  drawHexagon(getCellCenter(cells[kingCell]), boardView.scale)
}

function drawText(text, cell, side){
	ctx.save()
	var metric = ctx.measureText(text)
	var pos = getCellCenter(cell)
	ctx.translate(pos.x, pos.y)
	ctx.rotate(-Math.PI*side/3)
	ctx.fillText(text, -metric.width/2, -boardView.scale*9/16);
	ctx.restore()
}

function drawCells(){
	ctx.strokeStyle = 'black';
	ctx.lineWidth = boardView.scale/25
	colors = getCellColors()
	for(var key in cells){
		ctx.fillStyle = colors[cells[key].pos.y%3]
		drawCell(cells[key])
	}
	if(gameStatus){
	  if(gameStatus.whiteCheck) drawCheckWarning("WHITE")
    if(gameStatus.blackCheck) drawCheckWarning("BLACK")
	}
	for(var key in cells){
		drawCellDetails(cells[key])
	}
}

function drawLabels(){
	ctx.fillStyle = 'black'
	ctx.font = boardView.scale*2/5+'px Georgia'
	var n = gameSettings.boardSize-1
  drawText('A', cells['A'+n+n], 2)
	for(var i = 0; i <= n; i++) drawText(i, cells['A'+i+n], 1)
	for(var i = 0; i <= n; i++) drawText(i, cells['A'+n+i], 3)
	drawText('C', cells['C'+n+n], 0)
	for(var i = 1; i <= n; i++) drawText(i, cells['C'+i+n], 1)
	for(var i = 0; i <= n; i++) drawText(i, cells['C'+n+i], 5)
	drawText('E', cells['E'+n+n], 4)
	for(var i = 1; i <= n; i++) drawText(i, cells['E'+i+n], 5)
	for(var i = 1; i <= n; i++) drawText(i, cells['E'+n+i], 3)
}

function enableShadow(){
  ctx.shadowColor = 'rgba(0,0,0,0.4)'
  ctx.shadowBlur = boardView.scale*8/50
  ctx.imageSmoothingQuality = "high"
}

function updateScale(){
  var n = gameSettings.boardSize
  var w = canvas.offsetWidth
  var h = canvas.offsetHeight
  canvas.width = w
  canvas.height = h
  var cellsWidth = 3/4*(2*n-2)+1
  var cellsHeight = Math.sqrt(3)/2*(2*n-2)+1
  boardView = {}
  boardView.scale = Math.min(w/(1+cellsWidth), h/(1+cellsHeight))
  boardView.padding = {
    x:Math.max(boardView.scale/2, (w-cellsWidth*boardView.scale)/2),
    y:Math.max(boardView.scale/2, (h-cellsHeight*boardView.scale)/2)
  }
}

function drawFinalMessage(text, color){
	ctx.fillStyle = color
	ctx.strokeStyle = "black"
	ctx.font = boardView.scale*1.5 + 'px Georgia'
  ctx.lineWidth = 1
  var metric = ctx.measureText(text)
  ctx.fillText(text, (canvas.width-metric.width)/2, canvas.height/2)
  ctx.strokeText(text, (canvas.width-metric.width)/2, canvas.height/2)
}

function drawBoard(){
	ctx.clearRect(0,0,canvas.width,canvas.height)
	updateScale()
	enableShadow()
	drawCells()
	drawLabels()
	if(finalMessage != null){
	  drawFinalMessage(finalMessage, finalMessageColor)
	}
}

function canMoveTo(figure, destCell){
  var replacedFigure = cells[destCell].figure
  var fromCell = figure.cell
  cells[figure.cell].figure = null
  cells[destCell].figure = figure
  figure.cell = destCell

  kingCell = findKing(figure.player)
  var answer = true
  for(var key in cells){
    if(cells[key].figure != null && cells[key].figure.player !== figure.player){
      var nearCells = cells[key].figure.getNearCells()
      for(var i in nearCells){
        if(nearCells[i].name === kingCell) answer = false
      }
    }
  }

  cells[fromCell].figure = figure
  cells[destCell].figure = replacedFigure
  figure.cell = fromCell
  return answer
}

function clearMark(){
	for(var key in cells){
		cells[key].underMove = false
	}
}

function markMoveAndAttackCells(figure){
	clearMark()
	var nearCells = figure.getNearCells()
	for(var i = 0; i < nearCells.length; i++){
		if(canMoveTo(figure, nearCells[i].name)) cells[nearCells[i].name].underMove = true
	}
	drawBoard()
}

function getClickedCell(e){
	for(var key in cells){
		if(insideCell(cells[key], e.layerX, e.layerY)) return key
	}
	return null
}

function addFiguresToBoard(figures){
  for(var key in cells){
    cells[key].figure = null
  }
  var figureDrawFactory = getFigureDrawFactory()
  for (var i = 0; i < figures.length; i++){
    cells[figures[i].cell].figure = makeFigure(figures[i], figureDrawFactory)
  }
  drawBoard()
}

function initCells(){
  for(var key in cells){
	  cells[key].underMove = false
	  cells[key].figure = null
  }
}

function updateSettings(settings){
  gameSettings = settings
}

function loadUserSettings(callback){
  var xhr = new XMLHttpRequest()
  xhr.open('GET', '/data/currentUserSettings')
  xhr.onload = () => {
    if(xhr.response != ''){
      userSettings = JSON.parse(xhr.response)
      callback()
    }
  }
  xhr.send()
}

function updateFinalMessage(winner){
  if(winner != null){
    if(winner === "UNKNOWN"){
      finalMessage = null
      finalMessageColor = null
    }else if(winner === this.player.color){
      finalMessage = "Победа"
      finalMessageColor = "lime"
    }else if(winner === "DRAW"){
      finalMessage = "Ничья"
      finalMessageColor = "yellow"
    }else{
      finalMessage = "Поражение"
      finalMessageColor = "red"
    }
  }
}

var boardView = null
var cells = null
var gameSettings = null
var gameStatus = null
var userSettings = null
var imagePool = {}
var loadingImage = 0

var finalMessage = null
var finalMessageColor = null

var canvas = document.getElementById('canvas'),
    ctx     = canvas.getContext('2d')