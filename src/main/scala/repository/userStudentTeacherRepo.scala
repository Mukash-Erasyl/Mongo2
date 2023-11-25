package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}


import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.sys.SystemProperties.headless.key


object StudentTeacherRepo {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllUsers(): Future[List[User1]] = {
    val futureUsers = MongoDBConnection.userStuDentTeacher.find().toFuture();
    futureUsers.map { docs =>
      Option(docs).map(_.map { doc =>
        val userType = doc.getString("userType")

        val user: User1 = userType match {
          case "студент" =>

            UserStudent(
              id = doc.getInteger("id"),
              login = doc.getString("login"),
              password = doc.getString("password"),
              userType = doc.getString("userType"),
              name = doc.getString("name"),
              birthDate = doc.getString("birthDate"),
              gender = doc.getString("gender"),
              nationality = doc.getString("nationality"),
              address = doc.getString("address"),
              faculty = doc.getInteger("faculty"),
              course = doc.getInteger("course"),
              specialization = doc.getInteger("specialization"),
              scheduleId = doc.getInteger("scheduleId"),
              gradesIds = Option(doc.getList("gradesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
              subjectsIds = Option(doc.getList("subjectsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
              teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
              status = doc.getString("status")
            )

          case "Преподаватель" => UserTeacher(
            id = doc.getInteger("id"),
            login = doc.getString("login"),
            password = doc.getString("password"),
            userType = doc.getString("userType"),
            name = doc.getString("name"),
            birthDate = doc.getString("birthDate"),
            gender = doc.getString("gender"),
            nationality = doc.getString("nationality"),
            address = doc.getString("address"),
            disciplinesIds = doc.getList("DisciplinesID", classOf[Integer]).asScala.map(_.toInt).toList,
            education = doc.getString("Education"),
            qualification = doc.getString("Qualification"),
            experience = doc.getInteger("Experience"),
            scheduleId = doc.getInteger("ScheduleId"),
            studentsIds = doc.getList("StudentsID", classOf[Integer]).asScala.map(_.toInt).toList,
            salary = doc.getInteger("Salary"),
            newsId = doc.getInteger("NewsID"),
            materialsId = doc.getInteger("MaterialsId"),
            position = doc.getString("Position"),
            awards = doc.getString("Awards"),
            certificationId = doc.getInteger("CertificationId"),
            attestationId = doc.getInteger("АттестацияID")

          )
        }

        user

      }.toList).getOrElse(List.empty)

    }
  }
}