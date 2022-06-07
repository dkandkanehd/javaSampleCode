package com.iwi.api.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.iwi.api.file.dao.FileDAO;
import com.iwi.comm.util.DateUtil;
import com.iwi.comm.util.PropertiesUtil;
import com.iwi.site.domain.ClubFileMng;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 1. ClassName    : 
 * 2. FileName     : FileService.java
 * 3. Package      : com.iwi.api.file.service
 * 4. Commnet      : 파일 서비스
 * 5. 작성자       : kms
 * 6. 작성일       : 2022. 5. 12. 오후 6:15:36
 */
@Service("fileService")
public class FileService extends EgovAbstractServiceImpl {

	private static final Logger log = LogManager.getLogger(FileService.class);
	
	//파일 기본 경로
	private String FILE_BASE_PATH = PropertiesUtil.getString("FILE_BASE_PATH");
	//설정된 파일 확장자
	private String WEB_FILE_EXT = PropertiesUtil.getString("WEB_FILE_EXT");
	// 최대 파일 크기 제한
	private String WEB_FILE_SIZE = PropertiesUtil.getString("WEB_FILE_SIZE"); 
	
	@Resource
	private FileDAO fileDAO;
	

	/**
	 * 1. MethodName    : uploadFile
	 * 2. ClassName     : FileService
	 * 3. Commnet       : 파일업로드 처리(단일, 다중 모두 사용가능)
	 * 4. 작성자        : kms
	 * 5. 작성일        : 2022. 5. 12. 오후 6:14:21
	 * @return List<ClubFileMng>
	 * @param request
	 * @param filePath
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public List<ClubFileMng> uploadFile(HttpServletRequest request, String filePath, String fileId) throws Exception{

		MultipartHttpServletRequest requestFiles = (MultipartHttpServletRequest) request;
		Iterator iteratorFile = requestFiles.getFileNames();
		
		//property에 설정된 크기 MB로 변경 (Mbyte * 1024(Kbyte) * 1024(byte)
		int permitSize = Integer.valueOf(WEB_FILE_SIZE);
		permitSize = permitSize * 1024 * 1024;
		
		//파일크기 총 합
		int intTotFileSize = 0;
		while(iteratorFile.hasNext()) {
			
			MultipartFile file = requestFiles.getFile((String)iteratorFile.next());
			
			intTotFileSize += file.getSize();
			
		}
		
		//파일크기 총 합이 설정된 MB 보다 작을경우 Exception
		if(intTotFileSize > permitSize) {
			throw new Exception();
			//throw new FileException("업로드할 파일 크기 총 용량이 너무 큽니다.(최대 "+WEB_FILE_SIZE+"MB)");
		} else {
			//iteration 초기화
			iteratorFile = requestFiles.getFileNames();
		}
		
		// return fileList 초기화
		List<ClubFileMng> fileList = new ArrayList<ClubFileMng>();
		
		// 파일 저장 경로 설정
		StringBuffer sbFileBasePath = new StringBuffer();
		sbFileBasePath.append(FILE_BASE_PATH);
		
		StringBuffer sbFileUnderPath = new StringBuffer();
		sbFileUnderPath.append(filePath + "/");
		sbFileUnderPath.append(DateUtil.getCurrentDateWithFormat("yyyyMM"));
		
		// 파일순번
		String strApplyFileSn = "";
		int applyFileSn = 0;
		// 파일 구분
		String applyFileCd = "";
		// 파일 설명
		String fileDesc = "";
		
		//반복문을 통한 파일 업로드 및 파일저장
		while(iteratorFile.hasNext()) {
			String fileCdName = (String)iteratorFile.next();
			MultipartFile file = requestFiles.getFile(fileCdName);
			
			if(file.getSize() > 0) {
				/* 파일 구분 및 설명 설정
				 * 01 : 스포츠클럽 등록신청서
				 * 02 : 정관
				 * 03 : 조직도
				 * 04 : 연간운영계획서
				 * 05 : 회원명부
				 * 06 : 회비납부내역서
				 * */ 
				if("file01".equals(fileCdName)) {
					applyFileCd = "01";
					fileDesc = "스포츠클럽 등록신청서";
				}else if("file02".equals(fileCdName)) {
					applyFileCd = "02";
					fileDesc = "정관";
				}else if("file03".equals(fileCdName)) {
					applyFileCd = "03";
					fileDesc = "조직도";
				}else if("file04".equals(fileCdName)) {
					applyFileCd = "04";
					fileDesc = "연간운영계획서";
				}else if("file05".equals(fileCdName)) {
					applyFileCd = "05";
					fileDesc = "회원명부";
				}else if("file06".equals(fileCdName)) {
					applyFileCd = "06";
					fileDesc = "회비납부내역서";
				}else {
					throw new Exception();
				}
				
				//원본 파일명
				String strOriginalFileName = file.getOriginalFilename();
				
				// 확장자
				String strFileExt = strOriginalFileName.substring(strOriginalFileName.lastIndexOf(".") + 1, strOriginalFileName.length());
				strFileExt = strFileExt.toLowerCase();
				
				if(WEB_FILE_EXT.indexOf(strFileExt) < 0) {
					throw new Exception();
				}
				
				//파일크기 체크
				if(file.getSize() > permitSize) {
					throw new Exception();
				}
				
				//파일순번 지정
				if("".equals(strApplyFileSn)) {
					strApplyFileSn = "1";
					applyFileSn = Integer.valueOf(strApplyFileSn);
				}else {
					applyFileSn += 1 ;
				}
				
				//현재 시간 구하기
				String pattern = "yyyyMMddhhmmss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String date = simpleDateFormat.format(new Date());
				
				//실제 저장파일 명
				String strSaveFileName = date + fileId + String.valueOf(applyFileSn) + "." + strFileExt;
				
				//지정된 경로에 파일 생성
				File saveFile = new File(sbFileBasePath.toString() + sbFileUnderPath.toString() + "/" + strSaveFileName);
				
				//폴더생성
				saveFile.mkdirs();
				
				//파일 없는경우 파일생성
				if(!saveFile.exists()) {
					saveFile.createNewFile();
				}
				
				//업로드된 파일 복사
				file.transferTo(saveFile);
				
				//파일 데이터 set
				ClubFileMng voFileTemp = new ClubFileMng();
				voFileTemp.setFileId(fileId);
				voFileTemp.setFileSn(String.valueOf(applyFileSn));
				voFileTemp.setFileSe(applyFileCd);
				voFileTemp.setFileDtlsSe("");
				voFileTemp.setFileOrgnlNm(strOriginalFileName);
				voFileTemp.setFileNm(strSaveFileName);
				voFileTemp.setFileDesc(fileDesc);
				voFileTemp.setFilePath(sbFileUnderPath.toString());
				voFileTemp.setFileSize(String.valueOf(file.getSize()));
				
				//파일 데이터 저장
				fileDAO.insertFile(voFileTemp);
				
				fileList.add(voFileTemp);
				
			}
			
		}
		
		return fileList;
	}	
	
	

	/**
	 * 1. MethodName    : selectFileId
	 * 2. ClassName     : FileService
	 * 3. Commnet       : 파일 ID SEQ값 조회 
	 * 4. 작성자        : kms
	 * 5. 작성일        : 2022. 5. 12. 오후 6:14:08
	 * @return int
	 * @return
	 * @throws Exception
	 */
	public int selectFileId() throws Exception{
		return fileDAO.selectFileId();
	}
	

	/**
	 * 1. MethodName    : downloadFile
	 * 2. ClassName     : FileService
	 * 3. Commnet       : 파일 다운로드 서비스 
	 * 4. 작성자        : kms
	 * 5. 작성일        : 2022. 5. 12. 오후 6:13:54
	 * @return void
	 * @param clubfileMng
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downloadFile(ClubFileMng clubfileMng, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		OutputStream fileOutput = null;
		FileInputStream fileInput = null;
		
		try {
			//파일정보의 실제 파일 위치경로
			StringBuffer sbFileBasePath = new StringBuffer();
			sbFileBasePath.append(FILE_BASE_PATH);
			sbFileBasePath.append(clubfileMng.getFilePath());
			
			fileOutput = response.getOutputStream();
			
			String fileOrginNm = clubfileMng.getFileOrgnlNm();
			response.setContentType("application/unknown");
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileOrginNm,"UTF-8"));
			
			//실제 파일경로를 토대로 파일을 생성한다.
			File downloadFile = new File(sbFileBasePath.toString() + "/" + clubfileMng.getFileNm());
			
			fileInput = new FileInputStream(downloadFile);
			
			byte[] b = new byte[4096];
			int size = 0;
			while((size = fileInput.read(b)) > 0) {
				
				fileOutput.write(b, 0, size);
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			//파일 객체 close
			if(fileOutput != null) {
				fileOutput.close();
			}
			if(fileInput != null) {
				fileInput.close();
			}
		}
	}
	
	/**
	 * 1. MethodName    : selectFileList
	 * 2. ClassName     : FileService
	 * 3. Commnet       : 파일 ID로 파일리스트 조회
	 * 4. 작성자        : kms
	 * 5. 작성일        : 2022. 5. 12. 오후 6:13:14
	 * @return List<ClubFileMng>
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public List<ClubFileMng> selectFileList(String fileId) throws Exception{
		return fileDAO.selectFileList(fileId);
	}
	
	/**
	 * 1. MethodName    : selectFileInfo
	 * 2. ClassName     : FileService
	 * 3. Commnet       : SEQ로 파일 정보 조회
	 * 4. 작성자        : kms
	 * 5. 작성일        : 2022. 5. 13. 오후 3:20:24
	 * @return ClubFileMng
	 * @param fileMngSeq
	 * @return
	 * @throws Exception
	 */
	public ClubFileMng selectFileInfo(String fileMngSeq) throws Exception{
		return fileDAO.selectFileInfo(fileMngSeq);
	}
	
}
