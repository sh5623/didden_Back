package com.diden.demo.error;

import com.diden.demo.error.exception.BadRequestException;
import com.diden.demo.error.exception.DataNotProcessExceptions;
import com.diden.demo.error.exception.NotFoundDataException;
import com.diden.demo.error.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GeneralExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({DataNotProcessExceptions.class, NullPointerException.class})
  public ExceptionVo dataNotProcessExceptions(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .message(e.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
    NotFoundDataException.class,
    BadRequestException.class,
    TokenException.class,
    IllegalArgumentException.class,
    HttpMessageNotReadableException.class,
    MissingServletRequestParameterException.class,
  })
  public ExceptionVo badRequest(final RuntimeException e) {
    log.error(e.toString());

    return ExceptionVo.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ExceptionVo constraintViolation(final ConstraintViolationException e) {
    log.error(e.toString());

    return ExceptionVo.builder()
        .message(
            e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .get())
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionVo processValidationError(final MethodArgumentNotValidException e) {
    log.error(e.toString());

    return ExceptionVo.builder()
        .message(
            e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .stream()
                .collect(Collectors.joining()))
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }
}