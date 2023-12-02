package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.Try

object SyllabusRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllSyllabuses(): Future[List[Syllabus]] = {
    val futureSyllabuses = MongoDBConnection.syllabusCollection.find().toFuture()

    futureSyllabuses.map { docs =>
      Option(docs).map(_.map { doc =>
        Syllabus(
          planId = doc.getInteger("planId"),
          name = doc.getString("name"),
          description = doc.getString("description"),
          credits = doc.getInteger("credits"),
          hours = doc.getInteger("hours"),
          disciplineType = doc.getString("disciplineType"),
          teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          departmentId = doc.getInteger("departmentId"),
          studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          materialsIds = Option(doc.getList("materialsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          topics = Option(doc.getList("topics", classOf[String])).map(_.asScala.toList).getOrElse(List.empty) ,
            classrooms = Option(doc.getList("classrooms", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          language = doc.getString("language"),
          schedulesIds = Option(doc.getList("schedulesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty)
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getSyllabusByPlanId(planId: String): Future[Option[Syllabus]] = {
    val syllabusDocument =Document("_id" -> new ObjectId(planId));

    MongoDBConnection.syllabusCollection.find(syllabusDocument).headOption().map {
      case Some(doc) =>
        Some(
          Syllabus(
            planId = doc.getInteger("planId"),
            name = doc.getString("name"),
            description = doc.getString("description"),
            credits = doc.getInteger("credits"),
            hours = doc.getInteger("hours"),
            disciplineType = doc.getString("disciplineType"),
            teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            departmentId = doc.getInteger("departmentId"),
            studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            materialsIds = Option(doc.getList("materialsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            topics = Option(doc.getList("topics", classOf[String])).map(_.asScala.toList).getOrElse(List.empty) ,
              classrooms = Option(doc.getList("classrooms", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            language = doc.getString("language"),
            schedulesIds = Option(doc.getList("schedulesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty)
          )
        )
      case None => None
    }
  }


  def findSyllabusByParams(param: String): Future[List[Syllabus]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = Try(keyValue(1).toInt).toOption

      val syllabusDocument = Document(key -> value)

      MongoDBConnection.syllabusCollection.find(syllabusDocument).toFuture().map { docs =>
        docs.map { doc =>
          Syllabus(
            planId = doc.getInteger("planId"),
            name = doc.getString("name"),
            description = doc.getString("description"),
            credits = doc.getInteger("credits"),
            hours = doc.getInteger("hours"),
            disciplineType = doc.getString("disciplineType"),
            teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            departmentId = doc.getInteger("departmentId"),
            studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            materialsIds = Option(doc.getList("materialsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            topics = Option(doc.getList("topics", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
            classrooms = Option(doc.getList("classrooms", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            language = doc.getString("language"),
            schedulesIds = Option(doc.getList("schedulesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty)
          )
        }.toList
      }
    } else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }

  def addSyllabus(syllabus: Syllabus): Future[String] = {
    val syllabusDocument = BsonDocument(
      "planId" -> BsonInt32(syllabus.planId),
      "name" -> BsonString(syllabus.name),
      "description" -> BsonString(syllabus.description),
      "credits" -> BsonInt32(syllabus.credits),
      "hours" -> BsonInt32(syllabus.hours),
      "disciplineType" -> BsonString(syllabus.disciplineType),
      "teachersIds" -> BsonArray(syllabus.teachersIds.map(BsonInt32(_))),
      "departmentId" -> BsonInt32(syllabus.departmentId),
      "studentsIds" -> BsonArray(syllabus.studentsIds.map(BsonInt32(_))),
      "materialsIds" -> BsonArray(syllabus.materialsIds.map(BsonInt32(_))),
      "topics" -> BsonArray(syllabus.topics.map(BsonString(_))),
      "classrooms" -> BsonArray(syllabus.classrooms.map(BsonInt32(_))),
      "language" -> BsonString(syllabus.language),
      "schedulesIds" -> BsonArray(syllabus.schedulesIds.map(BsonInt32(_)))
    )

    MongoDBConnection.syllabusCollection.insertOne(syllabusDocument).toFuture().map(_ => s"Syllabus with planId ${syllabus.planId} added successfully.")
  }

  def deleteSyllabus(planId: String): Future[String] = {
    val syllabusDocument = Document("_id" -> new ObjectId(planId));
    MongoDBConnection.syllabusCollection.deleteOne(syllabusDocument).toFuture().map(_ => s"Syllabus with planId $planId deleted successfully.")
  }

  def updateSyllabus(planId: String, updatedSyllabus: Syllabus): Future[String] = {
    val filter = Document("_id" -> new ObjectId(planId));

    val syllabusDocument = BsonDocument(
      "$set" -> BsonDocument(
        "planId" -> BsonInt32(updatedSyllabus.planId),
        "name" -> BsonString(updatedSyllabus.name),
        "description" -> BsonString(updatedSyllabus.description),
        "credits" -> BsonInt32(updatedSyllabus.credits),
        "hours" -> BsonInt32(updatedSyllabus.hours),
        "disciplineType" -> BsonString(updatedSyllabus.disciplineType),
        "teachersIds" -> BsonArray(updatedSyllabus.teachersIds.map(BsonInt32(_))),
        "departmentId" -> BsonInt32(updatedSyllabus.departmentId),
        "studentsIds" -> BsonArray(updatedSyllabus.studentsIds.map(BsonInt32(_))),
        "materialsIds" -> BsonArray(updatedSyllabus.materialsIds.map(BsonInt32(_))),
        "topics" -> BsonArray(updatedSyllabus.topics.map(BsonString(_))),
        "classrooms" -> BsonArray(updatedSyllabus.classrooms.map(BsonInt32(_))),
        "language" -> BsonString(updatedSyllabus.language),
        "schedulesIds" -> BsonArray(updatedSyllabus.schedulesIds.map(BsonInt32(_)))
      )
    )

    MongoDBConnection.syllabusCollection.updateOne(filter, syllabusDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Syllabus with planId $planId updated successfully."
      } else {
        s"Update for Syllabus with planId $planId not executed: Either there is an issue with the database or the provided planId doesn't exist."
      }
    }
  }
}
