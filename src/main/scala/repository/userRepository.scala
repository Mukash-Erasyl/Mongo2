package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}

import java.text.SimpleDateFormat
import scala.concurrent.{ExecutionContext, Future}


object UserRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllUsers(): Future[List[User]] = {
    val futureUsers = MongoDBConnection.userCollection.find().toFuture()

    futureUsers.map { docs =>
      Option(docs).map(_.map { doc =>
        User(
          id = doc.getInteger("id"),
          login = doc.getString("login"),
          password = doc.getString("password"),
          userType = doc.getString("userType"),
          name = doc.getString("name"),
          birthDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(doc.getString("birthDate")),
          gender = doc.getString("gender"),
          nationality = doc.getString("nationality"),
          address = doc.getString("address")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getUserById(userId: String): Future[Option[User]] = {
    val userDocument = Document("_id" -> new ObjectId(userId))

    MongoDBConnection.userCollection.find(userDocument).headOption().map {
      case Some(doc) =>
        Some(
          User(
            id = doc.getInteger("id"),
            login = doc.getString("login"),
            password = doc.getString("password"),
            userType = doc.getString("userType"),
            name = doc.getString("name"),
            birthDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(doc.getString("birthDate")),

            gender = doc.getString("gender"),
            nationality = doc.getString("nationality"),
            address = doc.getString("address")
          )
        )
      case None => None
    }
  }

  def findUserByParams(param: String): Future[Option[User]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = keyValue(1)

      val userDocument = key match {
        case "id" | "experience" | "scheduleId" | "salary" | "newsId" | "materialsId" | "certificationId" | "attestationId" =>
          Document(key -> value.toInt)
        case _ =>
          Document(key -> value)
      }

      MongoDBConnection.userCollection.find(userDocument).headOption().map {
        case Some(doc) =>
          Some(
            User(
              id = doc.getInteger("id"),
              login = doc.getString("login"),
              password = doc.getString("password"),
              userType = doc.getString("userType"),
              name = doc.getString("name"),
              birthDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(doc.getString("\nbirthDate")) ,
              gender = doc.getString("gender"),
              nationality = doc.getString("nationality"),
              address = doc.getString("address")
            )
          )
        case None => None
      }
    } else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }

  def addUser(user: User): Future[String] = {
    val userDocument = BsonDocument(
      "id" -> BsonInt32(user.id),
      "login" -> BsonString(user.login),
      "password" -> BsonString(user.password),
      "userType" -> BsonString(user.userType),
      "name" -> BsonString(user.name),
      "birthDate" -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(user.birthDate) ,
      "gender" -> BsonString(user.gender),
      "nationality" -> BsonString(user.nationality),
      "address" -> BsonString(user.address)
    )

    MongoDBConnection.userCollection.insertOne(userDocument).toFuture().map(_ =>
      s"Пользователь с id ${user.id} был добавлен, проверь БД ;)"
    )
  }

  def deleteUser(userId: String): Future[String] = {
    val userDocument = Document("_id" -> new ObjectId(userId))
    MongoDBConnection.userCollection.deleteOne(userDocument).toFuture().map(_ =>
      s"Пользователь с id ${userId} был удален, проверь БД ;)"
    )
  }

  def updateUser(userId: String, updatedUser: User): Future[String] = {
    val filter = Document("_id" -> new ObjectId(userId))

    val userDocument = BsonDocument(
      "$set" -> BsonDocument(
        "id" -> BsonInt32(updatedUser.id),
        "login" -> BsonString(updatedUser.login),
        "password" -> BsonString(updatedUser.password),
        "userType" -> BsonString(updatedUser.userType),
        "name" -> BsonString(updatedUser.name),
        "birthDate" -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(updatedUser.birthDate) ,
        "gender" -> BsonString(updatedUser.gender),
        "nationality" -> BsonString(updatedUser.nationality),
        "address" -> BsonString(updatedUser.address)
      )
    )

    MongoDBConnection.userCollection.updateOne(filter, userDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Пользователь был обновлен, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
      } else {
        "Обновление пользователя не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }
}
