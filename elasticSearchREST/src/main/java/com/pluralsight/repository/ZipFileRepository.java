package com.pluralsight.repository;

import com.model.ZipPath;

public interface ZipFileRepository {

	String getZipFileContent(ZipPath pathToZip);
}
