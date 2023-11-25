package MongoDBConnection
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._



object MongoDBConnection {
  private val mongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = mongoClient.getDatabase("disciplineUniver")
  val disciplineCollection: MongoCollection[Document] = database.getCollection("discipline")
  val departmentCollection: MongoCollection[Document] = database.getCollection("department")
  val facultyCollection: MongoCollection[Document] = database.getCollection("faculty")
  val scheduleCollection: MongoCollection[Document] = database.getCollection("schedule")
  val studentCollection: MongoCollection[Document] = database.getCollection("student")
  val syllabusCollection: MongoCollection[Document] = database.getCollection("syllabus")
  val teacherCollection: MongoCollection[Document] = database.getCollection("teacher")
  val userCollection: MongoCollection[Document] = database.getCollection("user")
  val userStuDentTeacher: MongoCollection[Document] = database.getCollection("userStuDentTeacher")
}