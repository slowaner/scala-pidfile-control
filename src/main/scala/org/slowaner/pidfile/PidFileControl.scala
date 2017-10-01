package org.slowaner.pidfile

import java.io._
import java.nio.file.Path

/**
  * Created by gunman on 07.04.2017.
  */
object PidFileControl {
  def lockInstance(file: File): Boolean = {
    var locked = false
    val lockFileName = if (file != null) file.getName else null
    try {
      val randomAccessFile = new RandomAccessFile(file, "rw")
      val fileLock = randomAccessFile.getChannel.tryLock()
      if (fileLock != null) {
        val bufWr = new BufferedWriter(new FileWriter(randomAccessFile.getFD))
        sys.addShutdownHook {
          try {
            // TODO: Correct resource managing. Make read available
            fileLock.release()
            bufWr.close()
            randomAccessFile.close()
            file.delete()
          } catch {
            case e: Throwable =>
              println("Unable to remove lock file: " + lockFileName + e)
          }
        }
        randomAccessFile.setLength(0)
        bufWr.write("123")
        bufWr.flush()
        locked = true
      }
    } catch {
      case e: Exception =>
        println("Unable to create and/or lock file: " + lockFileName + e)
    }
    locked
  }

  def lockInstance(lockFilePathName: String): Boolean = lockInstance(new File(lockFilePathName))

  def lockInstance(lockFilePath: Path): Boolean = lockInstance(lockFilePath.toFile)
}