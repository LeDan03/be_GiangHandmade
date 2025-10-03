package com.pandadev.gianghandmade.exceptions;

import com.pandadev.gianghandmade.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAll(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Lỗi hệ thống: "+ ex.getMessage(),500));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Tài khoản chưa được đăng ký hoặc mật khẩu không chính xác",400));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Không tìm thấy tài nguyên: "+ex.getMessage(),404));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Yêu cầu không hợp lệ: "+ex.getMessage(),400));
    }

    @ExceptionHandler(ConflicException.class)
    public ResponseEntity<ApiResponse> handleConflic(ConflicException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse("Xung đột tài nguyên: "+ex.getMessage(),409));
    }

    @ExceptionHandler(IllegalException.class)
    public ResponseEntity<ApiResponse> handleIllegal(IllegalException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Yêu cầu không hợp lệ: "+ex.getMessage(),400));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Không đủ quyền truy cập: "+ex.getMessage(),401));
    }
}
