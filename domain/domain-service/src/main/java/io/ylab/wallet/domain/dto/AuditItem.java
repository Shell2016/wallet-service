package io.ylab.wallet.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AuditItem {
    private LocalDateTime time;
    private String info;

    @Override
    public String toString() {
        return "{" +
                "time=" + time +
                ", info='" + info + '\'' +
                '}';
    }
}
