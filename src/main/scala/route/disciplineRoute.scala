package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import model.Discipline
import org.json4s.{DefaultFormats, jackson}
import repository._


object DisciplineRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization;
  implicit val formats = DefaultFormats;
  val route =
    pathPrefix("discipline") {
      concat(
        pathEnd {
          concat(
            get {
              complete(DisciplineRepository.getAllDiscipline())
            },
            post {
              entity(as[Discipline]) { discipline =>
                complete(DisciplineRepository.addDiscipline(discipline))
              }
            }
          )
        },
        path(Segment) { disciplineId =>
          concat(
            get {
              complete(DisciplineRepository.getDisciplineById(disciplineId))
            },
            put {
              entity(as[Discipline]) { updatedDiscipline =>
                complete(DisciplineRepository.updateDiscipline(disciplineId, updatedDiscipline))
              }
            },
            delete {
              complete(DisciplineRepository.deleteDiscipline(disciplineId))
            }
          )
        }
      )
    }


}
