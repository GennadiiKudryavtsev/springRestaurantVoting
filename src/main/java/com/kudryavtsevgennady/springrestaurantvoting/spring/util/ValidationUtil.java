package com.kudryavtsevgennady.springrestaurantvoting.spring.util;

import com.kudryavtsevgennady.springrestaurantvoting.spring.HasId;
import com.kudryavtsevgennady.springrestaurantvoting.spring.util.exception.ErrorType;
import com.kudryavtsevgennady.springrestaurantvoting.spring.util.exception.IllegalRequestDataException;
import javax.servlet.http.HttpServletRequest;
import javassist.NotFoundException;
import org.slf4j.Logger;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id, Class<T> clazz) throws NotFoundException {
        return checkNotFound(object, "type: " + clazz.getSimpleName() + ", id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) throws NotFoundException {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) throws NotFoundException {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) throws NotFoundException {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) throws NotFoundException {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable result = throwable;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}

