import java.time.Instant

import com.typesafe.scalalogging.StrictLogging
import net.logstash.logback.marker.Markers
import scalikejdbc.{ConnectionPool, DB, GlobalSettings, _}

object ScalikejdbcLogging extends App with StrictLogging {

  GlobalSettings.queryCompletionListener = (statement, params, millis) => {
    val marker = Markers.append(
      "sql",
      Map("statement" -> statement, "params" -> params, "millis" -> millis)
    )
    logger.info(marker, "Completed SQL execution")
  }

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1", "username", "password")

  // create table
  DB.localTx { implicit s =>
    sql"create table users(id bigint primary key auto_increment, name varchar(50) not null, created_at timestamp not null)"
      .update()
      .apply()
  }

  // insert
  DB.localTx { implicit s =>
    val name = "t.kadowaki"
    val createdAt = Instant.now()
    sql"insert into users(name, created_at) values($name, $createdAt)"
      .update()
      .apply()
  }

  // select
  DB.readOnly { implicit s =>
    val list = sql"select name, created_at from users"
      .map(rs => (rs.string("name"), rs.string("created_at")))
      .list()
      .apply()
    logger.info(s"Users: $list")
  }
}
