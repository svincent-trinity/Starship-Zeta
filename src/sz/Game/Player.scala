package sz.Game

import scalafx.scene.image.Image
import sz.Util.Vec2

/** The player representation for a simple game based on sprites. Handles all
 *  information regarding the player's positions, movements, and abilities.
 *
 *  @param avatar the image representing the player
 *  @param initPos the initial position of the '''center''' of the player
 *  @param bulletPic the image of the bullets fired by this player
 */
class Player(avatar:Image, initPos:Vec2, bulletPic:Image)
                extends Sprite(avatar, new Vec2(initPos)) {
  private val speed = 3
  private val leftVec = new Vec2(-speed, 0)
  private val rightVec = new Vec2(speed, 0)
  
  private val upVec = new Vec2(0, -speed)
  private val downVec = new Vec2(0, speed)
  
  private val bulletSpeed = new Vec2(0, -10)
  private val bulletOffset= new Vec2(0, -avatar.height.value/2)

  
  /** moves the player sprite one "step" to the left.  The amount of this
   *  movement will likely need to be tweaked in order for the movement to feel
   *  natural.
   *
   *  @return none/Unit
   */
  def moveLeft() { move(leftVec) }

  /** moves the player sprite one "step" to the right (see note above)
   *
   *  @return none/Unit
   */
  def moveRight() { move(rightVec) }
  
  def moveUp() { move(upVec) }
  
  def moveDown() { move(downVec) }
  
}
