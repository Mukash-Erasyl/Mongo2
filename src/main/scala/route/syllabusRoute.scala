package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import repository.SyllabusRepository
import model.Syllabus

object SyllabusRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val route =
    pathPrefix("syllabus") {
      concat(
        get {
          parameter("param") { param =>
            complete(SyllabusRepository.findSyllabusByParams(param.toString))
          }
        },
        pathEnd {
          concat(
            get {
              complete(SyllabusRepository.getAllSyllabuses())
            },
            post {
              entity(as[Syllabus]) { syllabus =>
                complete(SyllabusRepository.addSyllabus(syllabus))
              }
            }
          )
        },
        path(Segment) { planId =>
          concat(
            get {
              complete(SyllabusRepository.getSyllabusByPlanId(planId))
            },
            put {
              entity(as[Syllabus]) { updatedSyllabus =>
                complete(SyllabusRepository.updateSyllabus(planId, updatedSyllabus))
              }
            },
            delete {
              complete(SyllabusRepository.deleteSyllabus(planId))
            }
          )
        }
      )
    }
}
