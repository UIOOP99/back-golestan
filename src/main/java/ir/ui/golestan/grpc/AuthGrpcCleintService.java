package ir.ui.golestan.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ir.ui.golestan.authorization.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class AuthGrpcCleintService {

    public AuthenticatedUser signup(AuthenticatedUser user, String password)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("address", 0)//TODO put right values
        .usePlaintext()
                .build();

        AuthGoGrpcServiceGrpc.AuthGoGrpcServiceBlockingStub stub = AuthGoGrpcServiceGrpc.newBlockingStub(channel);
        ir.ui.golestan.grpc.User signedUpUser = stub.signup(authenticatedUserToUser(user, password));
        channel.shutdown();

        user.setUserId((int)signedUpUser.getID());
        return user;
    }

    public AuthPairToken login(String username, String password)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("address", 0)//TODO put right values
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

    public String refreshAccessToken(String token)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("address", 0)//TODO put right values
                .usePlaintext()
                .build();

        AuthGoGrpcServiceGrpc.AuthGoGrpcServiceBlockingStub stub = AuthGoGrpcServiceGrpc.newBlockingStub(channel);
        JWTToken jwtToken = stub.refreshAccessToken(JWTToken.newBuilder().setToken(token).build());
        channel.shutdown();

        return jwtToken.getToken();
    }


    private ir.ui.golestan.grpc.User authenticatedUserToUser(AuthenticatedUser authenticatedUser,  String password)
    {
        return ir.ui.golestan.grpc.User.newBuilder()
                .setEmail(authenticatedUser.getEmail())
                .setFirstName(authenticatedUser.getFirstName())
                .setLastName(authenticatedUser.getLastName())
                .setUsername(authenticatedUser.getUsername())
//                .setRoleId() //TODO it needs roleId, idk what is roleId
                .setPassword(password)
//                .setGender() //TODO we dont have gender
        .build();
    }

}
