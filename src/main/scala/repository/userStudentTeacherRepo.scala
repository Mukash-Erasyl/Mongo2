package repository

import MongoDBConnection._
import model._
import org.json4s.JsonAST.JString
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonDocument, ObjectId}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala


object StudentTeacherRepo {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllUsers(): Future[List[User1]] = {
    val futureUsers = MongoDBConnection.userStuDentTeacher.find().toFuture();
    futureUsers.map { docs =>
      Option(docs).map(_.map { doc =>
        val userType = doc.getString("userType")

        val user: User1 = userType match {
          case "Студент" =>

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

  def getAllUsersById(userId: String): Future[Option[User1]] = {
    val userDocument = Document("_id" -> new ObjectId(userId))
    val futureUsers = MongoDBConnection.userStuDentTeacher.find(userDocument).headOption().map {
      case Some(doc) =>
        val userType = doc.getString("userType")
        val user: Option[User1] = userType match {
          case "студент" =>
            Some(
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
            )

          case "Преподаватель" =>
            Some(
              UserTeacher(
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
            )
        }
        user

      case None => None
    }

    futureUsers
  }


  def addUser(userObj: TeacherUser): Future[String]  = {
    val userDocument = userObj.userType match {
      case "студент" =>
        BsonDocument(
          "id" -> userObj.id,
          "login" -> userObj.login,
          "password" -> userObj.password,
          "userType" -> userObj.userType,
          "name" -> userObj.name,
          "birthDate" -> userObj.birthDate,
          "gender" -> userObj.gender,
          "nationality" -> userObj.nationality,
          "address" -> userObj.address,
          "faculty" -> userObj.faculty,
          "course" -> userObj.course,
          "specialization" -> userObj.specialization,
          "scheduleId" -> userObj.scheduleId,
          "gradesIds" -> userObj.gradesIds,
          "subjectsIds" -> userObj.subjectsIds,
          "teachersIds" -> userObj.teachersIds,
          "status" -> userObj.status
        )

      case "Преподаватель" =>
        BsonDocument(
          "id" -> userObj.id,
          "login" -> userObj.login,
          "password" -> userObj.password,
          "userType" -> userObj.userType,
          "name" -> userObj.name,
          "birthDate" -> userObj.birthDate,
          "gender" -> userObj.gender,
          "nationality" -> userObj.nationality,
          "address" -> userObj.address,
          "disciplinesIds" -> userObj.disciplinesIds,
          "education" -> userObj.education,
          "qualification" -> userObj.qualification,
          "experience" -> userObj.experience,
          "scheduleId" -> userObj.scheduleId,
          "studentsIds" -> userObj.studentsIds,
          "salary" -> userObj.salary,
          "newsId" -> userObj.newsId,
          "materialsId" -> userObj.materialsId,
          "position" -> userObj.position,
          "awards" -> userObj.awards,
          "certificationId" -> userObj.certificationId,
          "attestationId" -> userObj.attestationId
        )
    }

    MongoDBConnection.userStuDentTeacher.insertOne(userDocument).toFuture().map(_ =>
      s"Пользователь с id ${userObj.id} - ${userObj.userType} был добавлен, проверь БД ;)"
    )


  }

  def deleteUser(userId: String): Future[String] = {
    val userDocument = Document("_id" -> new ObjectId(userId))
    MongoDBConnection.userStuDentTeacher.deleteOne(userDocument).toFuture().map(_ =>
      s"Пользователь с id ${userId} был удален, проверь БД ;)"
    )
  }

    def updateUser(userId: String, userObj: TeacherUser): Future[String] = {
          val filter = Document("_id" -> new ObjectId(userId))
      val userDocument = userObj.userType match {
        case "студент" =>
          BsonDocument(
            "$set" -> BsonDocument(
            "id" -> userObj.id,
            "login" -> userObj.login,
            "password" -> userObj.password,
            "userType" -> userObj.userType,
            "name" -> userObj.name,
            "birthDate" -> userObj.birthDate,
            "gender" -> userObj.gender,
            "nationality" -> userObj.nationality,
            "address" -> userObj.address,
            "faculty" -> userObj.faculty,
            "course" -> userObj.course,
            "specialization" -> userObj.specialization,
            "scheduleId" -> userObj.scheduleId,
            "gradesIds" -> userObj.gradesIds,
            "subjectsIds" -> userObj.subjectsIds,
            "teachersIds" -> userObj.teachersIds,
            "status" -> userObj.status
            )
          )

        case "Преподаватель" =>
          BsonDocument(
            "$set" -> BsonDocument(
            "id" -> userObj.id,
            "login" -> userObj.login,
            "password" -> userObj.password,
            "userType" -> userObj.userType,
            "name" -> userObj.name,
            "birthDate" -> userObj.birthDate,
            "gender" -> userObj.gender,
            "nationality" -> userObj.nationality,
            "address" -> userObj.address,
            "disciplinesIds" -> userObj.disciplinesIds,
            "education" -> userObj.education,
            "qualification" -> userObj.qualification,
            "experience" -> userObj.experience,
            "scheduleId" -> userObj.scheduleId,
            "studentsIds" -> userObj.studentsIds,
            "salary" -> userObj.salary,
            "newsId" -> userObj.newsId,
            "materialsId" -> userObj.materialsId,
            "position" -> userObj.position,
            "awards" -> userObj.awards,
            "certificationId" -> userObj.certificationId,
            "attestationId" -> userObj.attestationId
            )
          )

      }
      MongoDBConnection.userStuDentTeacher.updateOne(filter, userDocument).toFuture().map { updatedResult =>
        if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
          s"Пользователь был обновлен, id: ${filter} - ${userObj.userType} - не знаю, что изменилось, проверяй в БД ;)"
        } else {
          "Обновление пользователя не выполнено: Проблема либо в базе, либо в тебе ;)"
        }
      }

    }


//  def updateUser(userId: String, updatedUser: User): Future[String] = {
//    val filter = Document("_id" -> new ObjectId(userId))
//
//    val userDocument = BsonDocument(
//      "$set" -> BsonDocument(
//        "id" -> BsonInt32(updatedUser.id),
//        "login" -> BsonString(updatedUser.login),
//        "password" -> BsonString(updatedUser.password),
//        "userType" -> BsonString(updatedUser.userType),
//        "name" -> BsonString(updatedUser.name),
//        "birthDate" -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(updatedUser.birthDate),
//        "gender" -> BsonString(updatedUser.gender),
//        "nationality" -> BsonString(updatedUser.nationality),
//        "address" -> BsonString(updatedUser.address)
//      )
//    )
//
//    MongoDBConnection.userCollection.updateOne(filter, userDocument).toFuture().map { updatedResult =>
//      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
//        s"Пользователь был обновлен, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
//      } else {
//        "Обновление пользователя не выполнено: Проблема либо в базе, либо в тебе ;)"
//      }
//    }
//  }

}