package controllers

import javax.inject.Inject
import models._
import play.api.mvc.ControllerComponents

class StatefulController @Inject() (cc: ControllerComponents) extends SchemalessControllerBase(cc) {

  import Implicits._

  def create(history: String, element: String) = perform[Add.type](history, maybeElement = Some(element))
  def update(history: String, id: Long, element: String) = perform[Mutate.type](history, Some(element), Some(id))
  def delete(history: String, id: Long) = perform[Subtract.type](history, maybeId = Some(id))
}
