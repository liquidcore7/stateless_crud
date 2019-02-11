package controllers

import javax.inject.Inject

import play.api.mvc._


class DummyController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc) {

  def index = Action {
    Ok("Not implemented.")
  }
}
