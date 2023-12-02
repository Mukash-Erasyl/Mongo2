package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.Try;

object ScheduleRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllSchedules(): Future[List[Schedule]] = {
    val futureSchedules = MongoDBConnection.scheduleCollection.find().toFuture()

    futureSchedules.map { docs =>
      Option(docs).map(_.map { doc =>
        Schedule(
          scheduleId = doc.getInteger("ScheduleID"),
          year = doc.getInteger("Year"),
          semester = doc.getInteger("Semester"),
          faculty = doc.getString("Faculty"),
          group = doc.getInteger("Group"),
          specialization = doc.getString("Specialization"),
          weekday = doc.getString("Weekday"),
          startTime = doc.getString("StartTime"),
          endTime = doc.getString("EndTime"),
          classroom = doc.getInteger("Classroom"),
          teacherId = doc.getInteger("TeacherID"),
          disciplineId = doc.getInteger("Discipline"),
          teacherISS = doc.getInteger("TeacherISS")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getScheduleById(scheduleId: String): Future[Option[Schedule]] = {
    val scheduleDocument = Document("_id" -> new ObjectId(scheduleId))

    MongoDBConnection.scheduleCollection.find(scheduleDocument).headOption().map {
      case Some(doc) =>
        Some(
          Schedule(
            scheduleId = doc.getInteger("ScheduleID"),
            year = doc.getInteger("Year"),
            semester = doc.getInteger("Semester"),
            faculty = doc.getString("Faculty"),
            group = doc.getInteger("Group"),
            specialization = doc.getString("Specialization"),
            weekday = doc.getString("Weekday"),
            startTime = doc.getString("StartTime"),
            endTime = doc.getString("EndTime"),
            classroom = doc.getInteger("Classroom"),
            teacherId = doc.getInteger("TeacherID"),
            disciplineId = doc.getInteger("Discipline"),
            teacherISS = doc.getInteger("TeacherISS")
          )
        )
      case None => None
    }
  }

  def findScheduleByParams(param: String): Future[List[Schedule]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = Try(keyValue(1).toInt).toOption

      val scheduleDocument = Document(key -> value)

      MongoDBConnection.scheduleCollection
        .find(scheduleDocument)
        .toFuture()
        .map { docs =>
          docs.map { doc =>
            Schedule(
              scheduleId = doc.getInteger("ScheduleID"),
              year = doc.getInteger("Year"),
              semester = doc.getInteger("Semester"),
              faculty = doc.getString("Faculty"),
              group = doc.getInteger("Group"),
              specialization = doc.getString("Specialization"),
              weekday = doc.getString("Weekday"),
              startTime = doc.getString("StartTime"),
              endTime = doc.getString("EndTime"),
              classroom = doc.getInteger("Classroom"),
              teacherId = doc.getInteger("TeacherID"),
              disciplineId = doc.getInteger("Discipline"),
              teacherISS = doc.getInteger("TeacherISS")
            )
          }.toList
        }
    } else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }


  def addSchedule(schedule: Schedule): Future[String] = {
    val scheduleDocument = BsonDocument(
      "ScheduleID" -> BsonInt32(schedule.scheduleId),
      "Year" -> BsonInt32(schedule.year),
      "Semester" -> BsonInt32(schedule.semester),
      "Faculty" -> BsonString(schedule.faculty),
      "Group" -> BsonInt32(schedule.group),
      "Specialization" -> BsonString(schedule.specialization),
      "Weekday" -> BsonString(schedule.weekday),
      "StartTime" -> BsonString(schedule.startTime),
      "EndTime" -> BsonString(schedule.endTime),
      "Classroom" -> BsonInt32(schedule.classroom),
      "TeacherID" -> BsonInt32(schedule.teacherId),
      "Discipline" -> BsonInt32(schedule.disciplineId),
      "TeacherISS" -> BsonInt32(schedule.teacherISS)
    )

    MongoDBConnection.scheduleCollection.insertOne(scheduleDocument).toFuture().map(_ => s"Расписание - ${schedule.scheduleId} было добавлено, проверь БД ;)")
  }

  def deleteSchedule(scheduleId: String): Future[String] = {
    val scheduleDocument = Document("_id" -> new ObjectId(scheduleId))
    MongoDBConnection.scheduleCollection.deleteOne(scheduleDocument).toFuture().map(_ => s"Расписание с id ${scheduleId} было удалено, проверь БД ;)")
  }

  def updateSchedule(scheduleId: String, updatedSchedule: Schedule): Future[String] = {
    val filter = Document("_id" -> new ObjectId(scheduleId))

    val scheduleDocument = BsonDocument(
      "$set" -> BsonDocument(
        "ScheduleID" -> BsonInt32(updatedSchedule.scheduleId),
        "Year" -> BsonInt32(updatedSchedule.year),
        "Semester" -> BsonInt32(updatedSchedule.semester),
        "Faculty" -> BsonString(updatedSchedule.faculty),
        "Group" -> BsonInt32(updatedSchedule.group),
        "Specialization" -> BsonString(updatedSchedule.specialization),
        "Weekday" -> BsonString(updatedSchedule.weekday),
        "StartTime" -> BsonString(updatedSchedule.startTime),
        "EndTime" -> BsonString(updatedSchedule.endTime),
        "Classroom" -> BsonInt32(updatedSchedule.classroom),
        "TeacherID" -> BsonInt32(updatedSchedule.teacherId),
        "Discipline" -> BsonInt32(updatedSchedule.disciplineId),
        "TeacherISS" -> BsonInt32(updatedSchedule.teacherISS)
      )
    )

    MongoDBConnection.scheduleCollection.updateOne(filter, scheduleDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Расписание было обновлено, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
      } else {
        "Обновление расписания не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }
}
