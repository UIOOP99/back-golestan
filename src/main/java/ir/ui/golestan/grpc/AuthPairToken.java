package ir.ui.golestan.grpc;

import lombok.Data;

@Data
public class AuthPairToken {

    private String refresh;
    private String access;
}
