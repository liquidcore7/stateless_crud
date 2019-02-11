package controllers

import javax.inject.Inject
import models.Read
import play.api.mvc.ControllerComponents

class StatelessController @Inject()(cc: ControllerComponents) extends SchemalessControllerBase(cc) {

  import models.Implicits.{read => rd}

  def get(history: String, id: Long) = perform[Read.type](history, maybeId = Some(id))(rd)

}
