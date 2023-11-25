package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala


object DisciplineRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllDiscipline(): Future[List[Discipline]] = {
    val futureDiscipline = MongoDBConnection.disciplineCollection.find().toFuture();

    futureDiscipline.map{ docs =>
      Option(docs).map(_.map { doc =>
        Discipline (
          disciplineId = doc.getInteger("DisciplineId"),
          disciplineName = doc.getString("DisciplineName"),
          description = doc.getString("Description"),
          credits = doc.getInteger("Credits"),
          hours = doc.getInteger("Hours"),
          disciplineType = doc.getString("DisciplineType"),
          teacherIds = Option(doc.getList("TeacherIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          department = doc.getString("Department"),
          studentIds = Option(doc.getList("StudentIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          topics = Option(doc.getList("Topics", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
          classrooms = Option(doc.getList("Classrooms", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          language = doc.getString("Language"),
          scheduleIds = Option(doc.getList("ScheduleIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
        )

      }.toList).getOrElse(List.empty)

    }

  }

  def getDisciplineById(disciplineId : String): Future[Option[Discipline]] = {
    val disciplineDocument = Document("_id" -> new ObjectId(disciplineId))

    MongoDBConnection.disciplineCollection.find(disciplineDocument).headOption().map {
      case Some(doc) =>
        Some(
          Discipline(
            disciplineId = doc.getInteger("DisciplineId"),
            disciplineName = doc.getString("DisciplineName"),
            description = doc.getString("Description"),
            credits = doc.getInteger("Credits"),
            hours = doc.getInteger("Hours"),
            disciplineType = doc.getString("DisciplineType"),
            teacherIds = Option(doc.getList("TeacherIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            department = doc.getString("Department"),
            studentIds = Option(doc.getList("StudentIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            topics = Option(doc.getList("Topics", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
            classrooms = Option(doc.getList("Classrooms", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            language = doc.getString("Language"),
            scheduleIds = Option(doc.getList("ScheduleIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          )
        )
        case None => None;
    }
  }

  def addDiscipline(discipline:Discipline) : Future[String] = {
    val disciplineDocument = BsonDocument(
      "DisciplineId"-> BsonInt32(discipline.disciplineId) ,
      "DisciplineName" -> BsonString(discipline.disciplineName) ,
      "Description" -> BsonString(discipline.description) ,
      "Credits" -> BsonInt32(discipline.credits) ,
      "Hours" -> BsonInt32(discipline.hours) ,
      "DisciplineType" -> BsonString(discipline.disciplineType) ,
      "TeacherIds" -> BsonArray(discipline.teacherIds.map(BsonInt32(_))) ,
      "Department" -> BsonString(discipline.department) ,
      "StudentIds" -> BsonArray(discipline.studentIds.map(BsonInt32(_))),
      "Topics" -> BsonArray(discipline.topics.map(BsonString(_))) ,
      "Classrooms" -> BsonArray(discipline.classrooms.map(BsonInt32(_))) ,
      "Language" -> BsonString(discipline.language) ,
      "ScheduleIds" -> BsonArray(discipline.scheduleIds.map(BsonInt32(_)))
    )
    MongoDBConnection.disciplineCollection.insertOne(disciplineDocument).toFuture().map(_=> s"Дисциплина - ${discipline.disciplineName} была добавлена , проверь БД ;)")
  }

  def deleteDiscipline(disciplineId:String) : Future[String] = {
    val disciplineDocument = Document("_id" -> new ObjectId(disciplineId));
    MongoDBConnection.disciplineCollection.deleteOne(disciplineDocument).toFuture().map(_=>s"Дисциплина с id ${disciplineId} была удалена , проверь БД ;)")
  }

  def updateDiscipline(disciplineId:String , updatedDiscipline:Discipline): Future[String] = {
    val filter = Document("_id" -> new ObjectId(disciplineId));

    val disciplineDocument = BsonDocument(
      "$set"-> BsonDocument(
        "DisciplineId" -> BsonInt32(updatedDiscipline.disciplineId),
        "DisciplineName" -> BsonString(updatedDiscipline.disciplineName),
        "Description" -> BsonString(updatedDiscipline.description),
        "Credits" -> BsonInt32(updatedDiscipline.credits),
        "Hours" -> BsonInt32(updatedDiscipline.hours),
        "DisciplineType" -> BsonString(updatedDiscipline.disciplineType),
        "TeacherIds" -> BsonArray(updatedDiscipline.teacherIds.map(BsonInt32(_))),
        "Department" -> BsonString(updatedDiscipline.department),
        "StudentIds" -> BsonArray(updatedDiscipline.studentIds.map(BsonInt32(_))),
        "Topics" -> BsonArray(updatedDiscipline.topics.map(BsonString(_))),
        "Classrooms" -> BsonArray(updatedDiscipline.classrooms.map(BsonInt32(_))),
        "Language" -> BsonString(updatedDiscipline.language),
        "ScheduleIds" -> BsonArray(updatedDiscipline.scheduleIds.map(BsonInt32(_)))

      )

    )

    MongoDBConnection.disciplineCollection.updateOne(filter , disciplineDocument).toFuture().map{updatedResult =>
      if(updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Дисциплина была обновлена , id : ${filter} - не знаю что изменилось , проверяй в БД ;)"
      }else {
        "Обновление дисциплины не выполнено : Проблема либо в базе , либо в тебе ;)"
      }
    }

  }

}
