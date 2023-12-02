package route


import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import repository.{StudentTeacherRepo, UserRepository}
import model.{TeacherUser, User, User1, UserStudent}

object ExRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val route =
    pathPrefix("exper") {
      concat(
        pathEnd {
          concat(
            get {
              complete(StudentTeacherRepo.getAllUsers())
            },
            post {
              entity(as[TeacherUser]) { user =>
                complete(StudentTeacherRepo.addUser(user))
              }
            }
          )
        },
        path(Segment) { userId =>
          concat(
            get {
              complete(StudentTeacherRepo.getAllUsersById(userId))
            },
            put {
              entity(as[TeacherUser]) { updatedUser =>
                complete(StudentTeacherRepo.updateUser(userId, updatedUser))
              }
            },
            delete {
              complete(StudentTeacherRepo.deleteUser(userId))
            }
          )
        }
      )
    }
}
