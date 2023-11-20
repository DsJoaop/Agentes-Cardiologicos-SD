package model;

import java.io.Serializable;
import java.util.UUID;

public class DadosServidor implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID messageId;
    private String mensagem;

    public DadosServidor(String mensagem1, UUID messageId) {
        this.messageId = messageId;
        this.mensagem = mensagem1;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
