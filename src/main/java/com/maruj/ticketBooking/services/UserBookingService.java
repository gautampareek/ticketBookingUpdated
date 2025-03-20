package com.maruj.ticketBooking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maruj.ticketBooking.Util.UserUtil;
import com.maruj.ticketBooking.entity.Ticket;
import com.maruj.ticketBooking.entity.Train;
import com.maruj.ticketBooking.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UserBookingService {
    private static final String PATH = "src/main/java/com/maruj/ticketBooking/localDB/userData.json";
    @Getter
    private User user;
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    @Getter
    private List<User> userList;

    private List<Train> trainsList;

    public UserBookingService(){
        loadUsers();
    }
    public UserBookingService(User user)  {
        this.user = user;
    }

    public Set<Train> getTrainsList() {
        loadUsers();
        Set<Train> trainSet = null;
        if(userList != null){
            trainSet = userList.stream().flatMap(user1 -> user1.getTicketsBooked().stream())
                    .map(ticket -> ticket.getTrain())
                    .collect(Collectors.toSet());
        }
        return trainSet;
    }

    public void loadUsers(){
        File file = new File(PATH);
        try {
            userList = mapper.readValue(file, new TypeReference<List<User>>(){});
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean ifUserExists(String loginName, String loginPassword){
        Optional<User> user = userList.stream()
                .filter(user1 -> user1.getName().equals(loginName) &&
                        user1.getHashedPassword().equals(UserUtil.getHashedPassword(loginPassword))).findFirst();
        if(user.isPresent()){
            this.user = user.get();
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public boolean signUp(String name, String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setHashedPassword(UserUtil.getHashedPassword(password));
        userList.add(user);
        try {
            addUserListToFile(userList);
            return Boolean.TRUE;
        }catch(IOException e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
    }

    private void addUserListToFile(List<User> userList) throws IOException {
        File file = new File(PATH);
        mapper.writeValue(file,userList);
    }

    public boolean bookTicket(Ticket ticket) {
        int userIndex = userList.indexOf(user);
        user.getTicketsBooked().add(ticket);
        userList.set(userIndex,user);
        try {
            addUserListToFile(userList);
            Train train = ticket.getTrain();
            List<List<Integer>> seats = train.getSeats();
            for(int i = 0;i<seats.size();i++){
                for(int j = 0;j<seats.get(i).size();j++){
                    if(seats.get(i).get(j) == 0){
                        seats.get(i).set(j,1);
                        break;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean ifTrainAvailable(String source, String destination, int trainNo) {
        Set<Train> trainSet = getTrainsList();

        return trainSet.stream().anyMatch(train -> train.getTrainNo().equals(String.valueOf(trainNo)) &&
                train.getStations().contains(source)
        && train.getStations().contains(destination)
        && train.getStations().indexOf(source) < train.getStations().indexOf(destination)
        );
    }

    public boolean ifTrainNotFullyBooked(int trainNo) {
        Set<Train> trainSet = getTrainsList();
        return trainSet.stream().filter(train -> train.getTrainNo().equals(String.valueOf(trainNo)))
                .flatMap(train -> train.getSeats().stream())
                .anyMatch(seat -> seat.stream().anyMatch(seatValue -> seatValue == 0));
    }

    public void cancelTicket(String ticketId) {
        Optional<Ticket> ticket = user.getTicketsBooked().stream()
                .filter(ticket1 -> ticket1.getTicketId().equals(ticketId)).findFirst();
        if(ticket.isPresent()){
            user.getTicketsBooked().remove(ticket.get());
            for (List<Integer> seat : ticket.get().getTrain().getSeats()) {
                if(seat.contains(1)){
                    seat.set(seat.indexOf(1), 0);
                    break;
                }
            }
            System.out.println("Ticket successfully cancelled!!!");
        }else{
            System.out.println("No Ticket found with this Ticket Number !!!!");
        }
    }
}
