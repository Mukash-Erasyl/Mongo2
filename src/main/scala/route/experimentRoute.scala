package route


import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import repository.{StudentTeacherRepo, UserRepository}
import model.User

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
            }
//            post {
//              entity(as[User]) { user =>
//                complete(UserRepository.addUser(user))
//              }
//            }
          )
        },
//        path(Segment) { userId =>
//          concat(
//            get {
//              complete(UserRepository.getUserById(userId))
//            },
//            put {
//              entity(as[User]) { updatedUser =>
//                complete(UserRepository.updateUser(userId, updatedUser))
//              }
//            },
//            delete {
//              complete(UserRepository.deleteUser(userId))
//            }
//          )
//        }
      )
    }
}
