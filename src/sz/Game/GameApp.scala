package sz.Game

import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import scalafx.scene.Scene
import scalafx.scene.canvas.GraphicsContext
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scalafx.scene.image.Image
import sz.Util.Vec2




object GameApp extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Starship Zeta"
    scene = new Scene {
      val w = 800
      val h = 500
      val canvas = new Canvas(w,h)
      val g:GraphicsContext = canvas.graphicsContext2D
      content = canvas
      val KeyPressSet = scala.collection.mutable.Set[String]()
      
      var accelFrameX = 0
      var accelFrameY = 0
      var inPlaceMid = true
      var inPlaceVert = true
      var quadrant = 1
      var gas = 500
      var coolDownWatch = 20
      var endGame = false
      var newQuad = true
      var planetsDiscovered = 0
      var lastPanel = "NONE"
      var decelXPos = 8
      var decelXNeg = 8
      var decelYPos = 8
      var decelYNeg = 8
      
      def updatePlanets(planBuff:scala.collection.mutable.ListBuffer[Planet], updateX:Int, updateY:Int) {
        for(plan <- planBuff) {
          plan.pos.x += updateX
          plan.pos.y += updateY
        }
      }
      
      def playerDiag() {
            if(KeyPressSet.contains("UP") && KeyPressSet.contains("RIGHT"))   player = new Player(upRight, player.pos, playerUp)
            if(KeyPressSet.contains("DOWN") && KeyPressSet.contains("RIGHT"))   player = new Player(downRight, player.pos, playerUp)

            if(KeyPressSet.contains("UP") && KeyPressSet.contains("LEFT"))   player = new Player(upLeft, player.pos, playerUp)
            if(KeyPressSet.contains("DOWN") && KeyPressSet.contains("LEFT"))   player = new Player(downLeft, player.pos, playerUp)

      }
      
      var asteroidsInQuad = 2*quadrant
      var planetsInQuad = quadrant
      
      val playerUp = new Image(("file:Images/playerUp.png"))
      val playerDown = new Image(("file:Images/playerDown.png"))
      val playerRight = new Image(("file:Images/playerRight.png"))
      val playerLeft = new Image(("file:Images/playerLeft.png"))
      val bluePlanet = new Image("file:Images/BluePlanet.png")
      val redPlanet = new Image("file:Images/RedPlanet.png")
      val yellowPlanet = new Image("file:Images/YellowPlanet.png")
      val earthPlanet = new Image("file:Images/Earth.png")
      val upLeft = new Image("file:Images/upLeft.png")
      val upRight = new Image("file:Images/upRight.png")
      val downLeft = new Image("file:Images/downLeft.png")
      val downRight = new Image("file:Images/downRight.png")

      
      val reputArr = Array(0,0)
      val planetsBuff = new scala.collection.mutable.ListBuffer[Planet]()
      
      val galaxy = new Image("file:Images/Galaxy-1.png")
      
      var player = new Player(playerUp, new Vec2(400,240), playerUp)
      
      
        canvas.onKeyPressed = (e:javafx.scene.input.KeyEvent) => {
        e.getCode match {
        //case javafx.scene.input.KeyCode.SPACE => { KeyPressSet += "SPACE" }
        case javafx.scene.input.KeyCode.SHIFT => { KeyPressSet += "SHIFT" }
        case javafx.scene.input.KeyCode.RIGHT => { /*reputArr(1) -= 1;*/ /*if(reputArr(1) > 800) player.moveLeft; */KeyPressSet += "RIGHT" }
        case javafx.scene.input.KeyCode.LEFT => { /*reputArr(1) += 1;  if(reputArr(1) < -800) player.moveRight;*/ KeyPressSet += "LEFT" }
        case javafx.scene.input.KeyCode.D => { /*reputArr(0) -= 1; if(reputArr(1) > 800) player.moveLeft; */KeyPressSet += "RIGHT" }
        case javafx.scene.input.KeyCode.A => { /*reputArr(1) += 1; if(reputArr(1) < -800) player.moveRight; */KeyPressSet += "LEFT" }
        case javafx.scene.input.KeyCode.UP => { /*reputArr(0) += 1;*/ KeyPressSet += "UP" }
        case javafx.scene.input.KeyCode.DOWN => { /*reputArr(0) -= 1;*/ KeyPressSet += "DOWN" }
        case javafx.scene.input.KeyCode.S => { /*reputArr(0) -= 1;*/ KeyPressSet += "DOWN" }
        case javafx.scene.input.KeyCode.W => { /*reputArr(0) += 1;*/ KeyPressSet += "UP" }
        case javafx.scene.input.KeyCode.R => { KeyPressSet -= "REVERSE" }
        //case javafx.scene.input.KeyCode.B => { startScreen = false }
        case _ => { }
        }
      }
      /*canvas.onMouseClicked = (e:javafx.scene.input.MouseEvent) => {
        e.getSource match {
          case javafx.scene.input.MouseButton.PRIMARY => {KeyPressSet += "CLICK"}
        }
      }*/
      /*canvas.onMouseReleased = (e:javafx.scene.input.MouseEvent) => {
        e.getSource match {
          case javafx.scene.input.MouseButton.PRIMARY => {KeyPressSet -= "CLICK"}
        }
      }*/
        canvas.onKeyReleased = (e:javafx.scene.input.KeyEvent) => {
        e.getCode match {
          //case javafx.scene.input.KeyCode.SPACE => { KeyPressSet -= "SPACE" }
          case javafx.scene.input.KeyCode.SHIFT => { KeyPressSet -= "SHIFT" }

          case javafx.scene.input.KeyCode.LEFT  => { /*reputArr(1) += 1;*/ KeyPressSet -= "LEFT" }
          case javafx.scene.input.KeyCode.D => { /*reputArr(1) -= 1;*/ KeyPressSet -= "RIGHT" }
          case javafx.scene.input.KeyCode.A => { /*reputArr(1) += 1;*/ KeyPressSet -= "LEFT" }
          case javafx.scene.input.KeyCode.S => { /*reputArr(0) -= 1;*/ KeyPressSet -= "DOWN" }
          case javafx.scene.input.KeyCode.W => { /*reputArr(0) += 1;*/ KeyPressSet -= "UP" }
          case javafx.scene.input.KeyCode.RIGHT => { /*reputArr(1) -= 1;*/ KeyPressSet -= "RIGHT" }
          case javafx.scene.input.KeyCode.UP => { /*reputArr(0) += 1;*/ KeyPressSet -= "UP" }
          case javafx.scene.input.KeyCode.DOWN => { /*reputArr(0) -= 1;*/ KeyPressSet -= "DOWN" }
          case javafx.scene.input.KeyCode.R => { KeyPressSet -= "REVERSE" }
          case _ => { }
        }
      }
      var oldTime:Long = 0
      val timer = AnimationTimer (t => {
        canvas.requestFocus

        //Use the oldTime variable to limit the timer firing rate
        if(t - oldTime > 1e9/60) {
          oldTime = t
          //Do the work that should happen every frame (redraw the screen)
          if(!endGame) {
          for(e <- KeyPressSet) {
            /*if(!KeyPressSet.contains("DOWN") && !KeyPressSet.contains("UP") && !KeyPressSet.contains("LEFT") && !KeyPressSet.contains("RIGHT")) {
              reputArr(0) += accelFrameX
              reputArr(1) += accelFrameY
              if(accelFrameX > 0) accelFrameX -= 1
              if(accelFrameY > 0) accelFrameY -= 1
            }*/
            /*if(KeyPressSet.contains("SPACE")) {
              gas -=1
            }*/
            if(inPlaceVert) {
            /*if(!KeyPressSet.contains("DOWN") && decelYPos >0) {
              reputArr(0) -= decelYPos
              updatePlanets(planetsBuff, 0, -4)
              decelYPos -= 1
            }
            if(!KeyPressSet.contains("UP") && decelYNeg > 0) {
              reputArr(0) -= decelYNeg
              updatePlanets(planetsBuff, 0, 4)
              decelYNeg -= 1
            }
            if(!KeyPressSet.contains("LEFT") && decelXPos > 0) {
              reputArr(0) += decelXPos
              updatePlanets(planetsBuff, 4, 0)
              decelXPos -= 1
            }
            if(!KeyPressSet.contains("DOWN") && decelXNeg > 0) {
              reputArr(0) -= decelXNeg
              updatePlanets(planetsBuff, -4, 0)
              decelXNeg -= 1
            }*/
            if(KeyPressSet.contains("DOWN")) {
              decelYPos = 8
              reputArr(0) -= 8//(8 + accelFrameY)
              player = new Player(playerDown, player.pos, playerUp)
              updatePlanets(planetsBuff, 0, -6)
            }
            if(KeyPressSet.contains("UP")) {
              decelYNeg = 30
              reputArr(0) += 8//(8 + accelFrameY)
              player = new Player(playerUp, player.pos, playerUp)
              updatePlanets(planetsBuff, 0, 6)

            }
            }
            if(inPlaceMid) {
            if(KeyPressSet.contains("RIGHT")) {
              decelXPos = 8
              reputArr(1) -= 8 /*+ accelFrameX)*/
              player = new Player(playerRight, player.pos, playerUp)
              updatePlanets(planetsBuff, -6, 0)

             // if(reputArr(1) > 800) player.moveLeft

            }
            if(KeyPressSet.contains("LEFT")) {
              decelXNeg = 8
              reputArr(1) += (8 + accelFrameX)
              //if(reputArr(1) < -800) player.moveRight
              player = new Player(playerLeft, player.pos, playerUp)
              updatePlanets(planetsBuff, 6, 0)

            }
            }
            if(inPlaceVert && inPlaceMid) {
            if(KeyPressSet.contains("LEFT") && KeyPressSet.contains("DOWN")) {
              reputArr(0) += 4
              reputArr(1) -= 4
              player = new Player(downLeft, player.pos, playerUp)

            }
            if(KeyPressSet.contains("LEFT") && KeyPressSet.contains("UP")) {
              reputArr(0) -= 4
              reputArr(1) -= 4
              player = new Player(upLeft, player.pos, playerUp)
            }
            if(KeyPressSet.contains("RIGHT") && KeyPressSet.contains("DOWN")) {
              reputArr(0) += 4
              reputArr(1) += 4
              player = new Player(downRight, player.pos, playerUp)
            }
            if(KeyPressSet.contains("RIGHT") && KeyPressSet.contains("UP")) {
              reputArr(0) -= 4
              reputArr(1) += 4
              player = new Player(upRight, player.pos, playerUp)
            }
            if(KeyPressSet.contains("SHIFT") && KeyPressSet.contains("UP")) {
              reputArr(0) -= 4
              updatePlanets(planetsBuff, 0, -3)
            }
            if(KeyPressSet.contains("SHIFT") && KeyPressSet.contains("DOWN")) {
              reputArr(0) += 4
              updatePlanets(planetsBuff, 0, 3)
            }
            if(KeyPressSet.contains("SHIFT") && KeyPressSet.contains("LEFT")) {
              reputArr(1) -= 4
              updatePlanets(planetsBuff, -3, 0)
            }
            if(KeyPressSet.contains("SHIFT") && KeyPressSet.contains("RIGHT")) {
              reputArr(1) += 4
              updatePlanets(planetsBuff, 3, 0)
            }
            }
              
          }
          
          g.fill = Color.Black
          g.fillRect(0,0, w,h) //blank out the screen
          
          g.drawImage(galaxy, reputArr(1), reputArr(0))
          g.drawImage(galaxy, reputArr(1)+800, reputArr(0))
          g.drawImage(galaxy, reputArr(1)-800, reputArr(0))
          g.drawImage(galaxy, reputArr(1), reputArr(0)+1000)
          g.drawImage(galaxy, reputArr(1), reputArr(0)-1000)
          g.drawImage(galaxy, reputArr(1)+800, reputArr(0)+1000)
          g.drawImage(galaxy, reputArr(1)-800, reputArr(0)+1000)
          g.drawImage(galaxy, reputArr(1)-800, reputArr(0)-1000)
          g.drawImage(galaxy, reputArr(1)+800, reputArr(0)-1000)

          if(reputArr(0) >= 950) {
            reputArr(0) = 950
            if(KeyPressSet.contains("UP")) { player.moveUp;               player = new Player(playerUp, player.pos, playerUp)
if(!KeyPressSet.contains("LEFT") && !KeyPressSet.contains("RIGHT")) player.moveUp; }
            if(KeyPressSet.contains("DOWN")) { player.moveDown;               player = new Player(playerDown, player.pos, playerUp)
if(!KeyPressSet.contains("LEFT") && !KeyPressSet.contains("RIGHT")) player.moveDown }
          playerDiag()
          }
          if(reputArr(0) <= -950) {
            reputArr(0) = -950
            if(KeyPressSet.contains("DOWN")) { player.moveDown;               player = new Player(playerDown, player.pos, playerUp)
if(!KeyPressSet.contains("LEFT") && !KeyPressSet.contains("RIGHT")) player.moveDown }
            if(KeyPressSet.contains("UP")) { player.moveUp;               player = new Player(playerUp, player.pos, playerUp)
if(!KeyPressSet.contains("LEFT") && !KeyPressSet.contains("RIGHT")) player.moveUp; }
            playerDiag
          }
          if(reputArr(1) >= 830) {
            reputArr(1) = 830 
            if(KeyPressSet.contains("LEFT")) { player.moveLeft;               player = new Player(playerLeft, player.pos, playerUp);
if(!KeyPressSet.contains("UP") && !KeyPressSet.contains("DOWN"))player.moveLeft }
            if(KeyPressSet.contains("RIGHT")) { player.moveRight;               player = new Player(playerRight, player.pos, playerUp)
if(!KeyPressSet.contains("UP") && !KeyPressSet.contains("DOWN"))player.moveRight }
            playerDiag
          }
          if(reputArr(1) <= -830) {
            reputArr(1) = -830
            if(KeyPressSet.contains("RIGHT")) { player.moveRight;               player = new Player(playerRight, player.pos, playerUp)
if(!KeyPressSet.contains("UP") && !KeyPressSet.contains("DOWN")) player.moveRight }
            if(KeyPressSet.contains("LEFT")) { player.moveLeft;               player = new Player(playerLeft, player.pos, playerUp)
if(!KeyPressSet.contains("UP") && !KeyPressSet.contains("DOWN")) player.moveLeft }
            playerDiag
          }
          
          for(plan <- planetsBuff) {
            plan.display(g)
            if(player.intersection(player.pos, plan.pos, 150)) {
              g.setFill(Color.AliceBlue)
              g.fillText("Press SHIFT to mine resources", plan.pos.x-70, plan.pos.y-110)
              if(KeyPressSet.contains("SHIFT")) gas = 500
              if(!plan.discovered) {
                planetsDiscovered += 1
                plan.discovered = true
              }
            }
          }

          
          if(player.pos.x <= 380 || player.pos.x >= 420) inPlaceMid = false
          if(player.pos.y <= 220 || player.pos.y >= 260) inPlaceVert = false
          if(player.pos.x >= 380 && player.pos.x <= 420) inPlaceMid = true
          if(player.pos.y >= 220 && player.pos.y <= 260) inPlaceVert = true
          if(player.pos.x > 750) { if(lastPanel != "RIGHT") {player.pos.x = 50; reputArr(1) = 830; quadrant += 1; newQuad = true; lastPanel = "LEFT"} else { g.setFill(Color.AliceBlue); g.fillText("Don't go back the way you came!", 550,player.pos.y-50); player.pos.x = 750}}
          if(player.pos.x < 50) { if(lastPanel != "LEFT") {player.pos.x = 750; reputArr(1) = -830; quadrant += 1; newQuad = true; lastPanel = "RIGHT"} else { g.setFill(Color.AliceBlue); g.fillText("Don't go back the way you came!", 50,player.pos.y-50); player.pos.x = 50}  }
          if(player.pos.y < 50) { if(lastPanel != "UP") {player.pos.y = 450; reputArr(0) = -950; quadrant += 1; newQuad = true; lastPanel = "DOWN"} else { g.setFill(Color.AliceBlue); g.fillText("Don't go back the way you came!", player.pos.x-100,player.pos.y+60); player.pos.y = 50}}
          if(player.pos.y > 450) { if(lastPanel != "DOWN") {player.pos.y = 50; reputArr(0) = 950; quadrant += 1; newQuad = true; lastPanel = "UP"} else { g.setFill(Color.AliceBlue); g.fillText("Don't go back the way you came!", player.pos.x-100,player.pos.y-60); player.pos.y = 450}}
          
          if(newQuad) {
            for(plan <- planetsBuff) {
              planetsBuff -= plan
            }
            var tmpQuad = 10-quadrant
            if(tmpQuad <= 2) tmpQuad = 2 
            while(tmpQuad != 0) {
              var imgPlan = bluePlanet
              val randomPlanetGenerate = scala.math.random
              if(randomPlanetGenerate < .25) imgPlan = bluePlanet
              else if (randomPlanetGenerate >= .25 && randomPlanetGenerate < .5) imgPlan = redPlanet
              else if (randomPlanetGenerate >=.5 && randomPlanetGenerate < .75) imgPlan = yellowPlanet
              else if (randomPlanetGenerate >= .75) imgPlan = earthPlanet
              val newPlan = new Planet(imgPlan, new Vec2(scala.util.Random.nextInt(1600)-800, scala.util.Random.nextInt(2000)-1000), false)
              planetsBuff += newPlan
              tmpQuad -= 1
              newQuad = false
            }
          }
         g.setFill(Color.ALICEBLUE)
         g.fillText("Planets discovered: " + planetsDiscovered, 10, 490)
          
          if(KeyPressSet.contains("LEFT") || KeyPressSet.contains("RIGHT") || KeyPressSet.contains("UP") || KeyPressSet.contains("DOWN")) {
            gas -= 1
            //if(accelFrameX < 15) accelFrameX += 1
            //if(accelFrameY < 15) accelFrameY += 1
          }
          if(gas <= 0) coolDownWatch -= 1
          g.setFill(Color.ALICEBLUE)
          g.fillRect(10, 10, gas+10, 20)
          g.setFill(Color.BROWN)
          g.fillText("FUEL", 20,25)
          if(coolDownWatch == 0) endGame = true
          
          
          
          player.display(g)
         } else if(endGame) {
           g.setFill(Color.BLACK)
           g.fillRect(0,0, 800,600)
           g.setFill(Color.AliceBlue)
           g.fillText("Game Over!", 50, 50)
           player.pos.x = 400
           player.pos.y = 240
           quadrant = 1
           gas = 500
           coolDownWatch = 20
           lastPanel = "NONE"
           planetsDiscovered = 0
           reputArr(0) = 0
           reputArr(1) = 0
           g.fillText("Press SHIFT to restart", 50, 100)
           if(KeyPressSet.contains("SHIFT")) endGame = false
           for(plan <- planetsBuff) planetsBuff -= plan
         }
      }
     })
     timer.start
  
    }
  }
}