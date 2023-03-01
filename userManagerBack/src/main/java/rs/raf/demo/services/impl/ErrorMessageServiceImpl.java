package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.services.ErrorMessageService;

import java.util.List;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;

    private final UserRepository userRepository;

    @Autowired
    public ErrorMessageServiceImpl(ErrorMessageRepository errorMessageRepository, UserRepository userRepository) {
        this.errorMessageRepository = errorMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ErrorMessage> getErrorMessages() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User u = this.userRepository.findByUsername(email);

        List<ErrorMessage> errorMessages = this.errorMessageRepository.findErrorMessageByMachineCreator(u);

        return errorMessages;


    }
}
