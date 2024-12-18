package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {

    private String from;
    private String to;
    private String text;
     private boolean isRead;
     private boolean typing;
     private boolean seen;





}
