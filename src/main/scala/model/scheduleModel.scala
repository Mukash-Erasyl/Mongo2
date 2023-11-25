package model

case class Schedule(
                     scheduleId: Int,
                     year: Int,
                     semester: Int,
                     faculty: String,
                     group: Int,
                     specialization: String,
                     weekday: String,
                     startTime: String,
                     endTime: String,
                     classroom: Int,
                     teacherId: Int,
                     disciplineId: Int,
                     teacherISS: Int
                   )

