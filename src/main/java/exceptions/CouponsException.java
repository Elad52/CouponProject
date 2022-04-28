package exceptions;

/*
Created by Elad on 30/03/2022

*/

import java.sql.Timestamp;
import java.time.Instant;

public class CouponsException extends java.lang.Exception {

    public CouponsException(String message) {
        super(message + " error Happened at " + Timestamp.from(Instant.now()));
    }
}
