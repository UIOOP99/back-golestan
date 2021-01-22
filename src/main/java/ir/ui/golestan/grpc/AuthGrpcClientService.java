package ir.ui.golestan.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.controller.AdminController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthGrpcClientService {

    private final GolestanConfiguration configuration;

    public int signup(AdminController.InputUser user) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(configuration.getAuthServerName(), configuration.getAuthServerPort())
                .usePlaintext()
                .build();

        AuthGoGrpcServiceGrpc.AuthGoGrpcServiceBlockingStub stub = AuthGoGrpcServiceGrpc.newBlockingStub(channel);
        ir.ui.golestan.grpc.User signedUpUser = stub.signup(inputUserToUser(user));
        channel.shutdown();

        return (int) signedUpUser.getID();
    }

    public AuthPairToken login(String username, String password) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(configuration.getAuthServerName(), configuration.getAuthServerPort())
                .usePlaintext()
                .build();

        AuthGoGrpcServiceGrpc.AuthGoGrpcServiceBlockingStub stub = AuthGoGrpcServiceGrpc.newBlockingStub(channel);
        PairToken pairToken = stub.login(Credentials.newBuilder().setUsername(username).setPassword(password).build());
        channel.shutdown();

        AuthPairToken authPairToken = new AuthPairToken();
        authPairToken.setAccess(pairToken.getAccess().getToken());
        authPairToken.setRefresh(pairToken.getRefresh().getToken());

        return authPairToken;
    }

    public String refreshAccessToken(String token) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(configuration.getAuthServerName(), configuration.getAuthServerPort())
                .usePlaintext()
                .build();

        AuthGoGrpcServiceGrpc.AuthGoGrpcServiceBlockingStub stub = AuthGoGrpcServiceGrpc.newBlockingStub(channel);
        JWTToken jwtToken = stub.refreshAccessToken(JWTToken.newBuilder().setToken(token).build());
        channel.shutdown();

        return jwtToken.getToken();
    }


    private ir.ui.golestan.grpc.User inputUserToUser(AdminController.InputUser inputUser) {
        return ir.ui.golestan.grpc.User.newBuilder()
                .setEmail(inputUser.getEmail())
                .setFirstName(inputUser.getFirstname())
                .setLastName(inputUser.getLastname())
                .setUsername(inputUser.getUsername())
//                .setRoleId() //TODO it needs roleId, idk what is roleId
                .setPassword(inputUser.getPassword())
//                .setGender() //TODO we dont have gender
                .build();
    }

}
