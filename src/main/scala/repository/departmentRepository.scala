package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala


object DepartmentRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllDepartments(): Future[List[Department]] = {
    val futureDepartments = MongoDBConnection.departmentCollection.find().toFuture()

    futureDepartments.map { docs =>
      Option(docs).map(_.map { doc =>
        Department(
          departmentId = doc.getInteger("departmentId"), // Переименовано
          name = doc.getString("name"),
          email = doc.getString("email"),
          number = doc.getString("number"),
          direction = doc.getString("direction"),
          headId = doc.getInteger("headId"),
          studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          disciplinesIds = Option(doc.getList("disciplinesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          publicationsIds = Option(doc.getList("publicationsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          collaboration = doc.getString("collaboration")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getDepartmentById(departmentId: String): Future[Option[Department]] = {
    val departmentDocument = Document("_id" -> new ObjectId(departmentId))

    MongoDBConnection.departmentCollection.find(departmentDocument).headOption().map {
      case Some(doc) =>
        Some(
          Department(
            departmentId = doc.getInteger("departmentId"), // Переименовано
            name = doc.getString("name"),
            email = doc.getString("email"),
            number = doc.getString("number"),
            direction = doc.getString("direction"),
            headId = doc.getInteger("headId"),
            studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            disciplinesIds = Option(doc.getList("disciplinesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            publicationsIds = Option(doc.getList("publicationsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            collaboration = doc.getString("collaboration")
          )
        )
      case None => None
    }
  }

  def addDepartment(department: Department): Future[String] = {
    val departmentDocument = BsonDocument(
      "departmentId" -> BsonInt32(department.departmentId),
      "name" -> BsonString(department.name),
      "email" -> BsonString(department.email),
      "number" -> BsonString(department.number),
      "direction" -> BsonString(department.direction),
      "headId" -> BsonInt32(department.headId),
      "studentsIds" -> BsonArray(department.studentsIds.map(BsonInt32(_))),
      "disciplinesIds" -> BsonArray(department.disciplinesIds.map(BsonInt32(_))),
      "publicationsIds" -> BsonArray(department.publicationsIds.map(BsonInt32(_))),
      "collaboration" -> BsonString(department.collaboration)
    )

    MongoDBConnection.departmentCollection.insertOne(departmentDocument).toFuture().map(_ => s"Департамент - ${department.name} был добавлен, проверь БД ;)")
  }

  def deleteDepartment(departmentId: String): Future[String] = {
    val departmentDocument = Document("_id" -> new ObjectId(departmentId))
    MongoDBConnection.departmentCollection.deleteOne(departmentDocument).toFuture().map(_ => s"Департамент с id ${departmentId} был удален, проверь БД ;)")
  }

  def updateDepartment(departmentId: String, updatedDepartment: Department): Future[String] = {
    val filter = Document("_id" -> new ObjectId(departmentId))

    val departmentDocument = BsonDocument(
      "$set" -> BsonDocument(
        "departmentId" -> BsonInt32(updatedDepartment.departmentId),
        "name" -> BsonString(updatedDepartment.name),
        "email" -> BsonString(updatedDepartment.email),
        "number" -> BsonString(updatedDepartment.number),
        "direction" -> BsonString(updatedDepartment.direction),
        "headId" -> BsonInt32(updatedDepartment.headId),
        "studentsIds" -> BsonArray(updatedDepartment.studentsIds.map(BsonInt32(_))),
        "disciplinesIds" -> BsonArray(updatedDepartment.disciplinesIds.map(BsonInt32(_))),
        "publicationsIds" -> BsonArray(updatedDepartment.publicationsIds.map(BsonInt32(_))),
        "collaboration" -> BsonString(updatedDepartment.collaboration)
      )
    )

    MongoDBConnection.departmentCollection.updateOne(filter, departmentDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Департамент был обновлен, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
      } else {
        "Обновление департамента не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }
}
