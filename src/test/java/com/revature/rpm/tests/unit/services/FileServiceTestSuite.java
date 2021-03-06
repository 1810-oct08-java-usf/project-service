package com.revature.rpm.tests.unit.services;

import static org.junit.Assert.assertTrue;

import com.revature.rpm.exceptions.FileSizeTooLargeException;
import com.revature.rpm.services.FileServiceImpl;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTestSuite {

  private FileServiceImpl classUnderTest = new FileServiceImpl();

  @Test
  public void T_download_Invalid() {
    String zipLink = "https://github.com/supertuxkart/stk-code";
    boolean thrown = false;
    try {
      File zipArchive = classUnderTest.download(zipLink + "/archive/master.zip");
      if (zipArchive.length() > 5500000) {
        throw new FileSizeTooLargeException(
            "The file size of: " + zipArchive.getName() + "exceeds limit");
      }
    } catch (FileSizeTooLargeException | IOException fstle) {

      System.out.println("FileSizeTooLargeException caught in test.");

      thrown = true;
    }
    assertTrue(thrown);
  }
}
