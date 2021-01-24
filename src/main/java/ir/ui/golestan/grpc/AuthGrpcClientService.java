package ir.ui.golestan.grpc;

import auth.AuthGrpc;
import auth.AuthOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.controller.AdminController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthGrpcClientService {

    private final GolestanConfiguration configuration;

    public int signup(String adminToken, AdminController.InputUser user) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(configuration.getAuthServerName(), configuration.getAuthServerPort())
                .usePlaintext()
                .build();

        Metadata metadata = new Metadata();
        Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(key, "Bearer " + adminToken);

        AuthGrpc.AuthBlockingStub stub = AuthGrpc.newBlockingStub(channel);
        stub = MetadataUtils.attachHeaders(stub, metadata);
        AuthOuterClass.User signedUpUser = stub.signup(inputUserToUser(user));
        channel.shutdown();

        return (int) signedUpUser.getID();
    }

    public AuthPairToken login(String username, String password) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(configuration.getAuthServerName(), configuration.getAuthServerPort())
                .usePlaintext()
                .build();

        AuthGrpc.AuthBlockingStub stub = AuthGrpc.newBlockingStub(channel);
        AuthOuterClass.PairToken pairToken = stub.login(AuthOuterClass.Credentials.newBuilder().setUsername(username).setPassword(password).build());
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

        AuthGrpc.AuthBlockingStub stub = AuthGrpc.newBlockingStub(channel);
        AuthOuterClass.JWTToken jwtToken = stub.refreshAccessToken(AuthOuterClass.JWTToken.newBuilder().setToken(token).build());
        channel.shutdown();

        return jwtToken.getToken();
    }


    private AuthOuterClass.User inputUserToUser(AdminController.InputUser inputUser) {
        return AuthOuterClass.User.newBuilder()
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
