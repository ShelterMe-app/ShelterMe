package org.ShelterMe.project.exceptions;

public class UsernameDoesNotExistException extends Exception{
    public UsernameDoesNotExistException(String username)
    {
        super(String.format("Username %s does not exist! If you don't have an account, please sign up.", username));
    }
}
