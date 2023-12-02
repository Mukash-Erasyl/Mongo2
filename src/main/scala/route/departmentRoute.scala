package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import model.Department
import org.json4s.{DefaultFormats, jackson}
import repository._


object DepartmentRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

//  complete(DepartmentRepository.findDepartmentsByParams(param.toString))

  val route =
    pathPrefix("department") {
      concat(
        get {
          parameter("param") { param =>
              complete(DepartmentRepository.findDepartmentsByParams(param.toString))
          }
        },
        pathEnd {
          concat(
            get {
              complete(DepartmentRepository.getAllDepartments())
            },
            post {
              entity(as[Department]) { department =>
                complete(DepartmentRepository.addDepartment(department))
              }
            }
          )
        },
        path(Segment) { departmentId =>
          concat(
            get {
              complete(DepartmentRepository.getDepartmentById(departmentId))
            },
            put {
              entity(as[Department]) { updatedDepartment =>
                complete(DepartmentRepository.updateDepartment(departmentId, updatedDepartment))
              }
            },
            delete {
              complete(DepartmentRepository.deleteDepartment(departmentId))
            }
          )
        }
      )
    }
}
