package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.entity.Message;
import uz.app.entity.User;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverAndIsReadFalse(User receiver);
    List<Message> findBySenderAndReceiver(User sender, User receiver);
}

