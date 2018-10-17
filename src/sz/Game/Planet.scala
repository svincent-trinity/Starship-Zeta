package sz.Game

import sz.Util.Vec2
import scalafx.scene.image.Image

class Planet(avatar:Image, pos:Vec2, discovered:Boolean) extends Sprite(avatar, new Vec2(pos)) {
  var discovered = false
}