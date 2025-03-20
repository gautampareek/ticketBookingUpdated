package com.maruj.ticketBooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maruj.ticketBooking.entity.Ticket;
import com.maruj.ticketBooking.entity.Train;
import com.maruj.ticketBooking.entity.User;
import com.maruj.ticketBooking.services.UserBookingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class TicketBookingApplication {

	public static void main(String[] args) {
		boolean loggedIn = false;
		SpringApplication.run(TicketBookingApplication.class, args);
		System.out.println("Spring boot application working successfully............");
		System.out.println("lets try if the dev tools are working or not");

		System.out.println("Running Train seat booking system");
		System.out.println("============================================================================================");
		System.out.println();
		System.out.println();
		UserBookingService userBookingService = new UserBookingService();

		Scanner sc = new Scanner(System.in);
		int userChoice = 0;
		while(userChoice != 8){
			System.out.println("Welcome to Train seat booking system");
			System.out.println("Enter your choice");
			System.out.println("1. Sign up");
			System.out.println("2. Login");
			System.out.println("3. Fetch Bookings");
			System.out.println("4. Book a ticket");
			System.out.println("5. Show Available Trains");
			System.out.println("6. Cancel Booking");
			System.out.println("7. Log Out");
			System.out.println("8. Exit");

			userChoice = sc.nextInt();
			switch (userChoice){
				case 1 :
					if(loggedIn){
						System.out.println("You are already logged in");
						break;
					}
					System.out.println("Enter your name");
					String name = String.valueOf(sc.next());
					System.out.println("Enter Your password");
					String password = String.valueOf(sc.next());
					if(userBookingService.signUp(name,password))
						System.out.println("Sign up successful, Please Login now....");
					else
						System.out.println("Sign up failed");
					break;
				case 2 :
					System.out.println("Enter your name");
					String loginName = String.valueOf(sc.next());
					System.out.println("Enter Your password");
					String loginPassword = String.valueOf(sc.next());
					if(userBookingService.ifUserExists(loginName,loginPassword)){
						System.out.println("Login successful");
					}else{
						System.out.println("User couldn't be found. Login failed!!!!!!!!");
					}
					loggedIn = true;
					break;
				case 3 :
					if(loggedIn){
					List<Ticket> tickets = userBookingService.getUser().getTicketsBooked();
					tickets.forEach(System.out::println);
					}else{
						System.out.println("Please Login first");
					}
					break;
				case 4 :
						if(loggedIn){
							System.out.println("Enter Source Station");
							String source = String.valueOf(sc.next());
							System.out.println("Enter Destination Station");
							String destination = String.valueOf(sc.next());
							System.out.println("Enter Train Number");

							int trainNo = sc.nextInt();
							if(userBookingService.ifTrainAvailable(source,destination,trainNo) && userBookingService.ifTrainNotFullyBooked(trainNo)){
								Ticket ticket = new Ticket();
								ticket.setSource(source);
								ticket.setDestination(destination);
								ticket.setUserId(userBookingService.getUser().getUserId());
								ticket.setDateOfTravel("2024-05-06");
								ticket.setTrain(userBookingService.getTrainsList().stream().filter(train1 -> train1.getTrainNo().equals(String.valueOf(trainNo))).findFirst().get());
								if(userBookingService.bookTicket(ticket)){
									System.out.println("Ticket Booked Successfully");
								}else {
									System.out.println("Ticket Booking failed");
								}

							}else
								System.out.println("Train not available or train is fully booked");
							}else {
							System.out.println("Please Login first");
						}
						break;
				case 5  :
						userBookingService.getTrainsList().forEach(System.out::println);
						break;
				case 6 :

						if(loggedIn){
							System.out.println("Enter Ticket Id");
							String ticketId = String.valueOf(sc.next());
							userBookingService.cancelTicket(ticketId);
						}else{
							System.out.println("Please Login first");
						}
						break;
				case 7 : loggedIn = false;
						 break;
			}

		}

	}
	public static List<User> getInitalUserData(){
		Train kotaExpress = new Train();
		kotaExpress.setTrainId("101");
		kotaExpress.setTrainNo("19807");
		kotaExpress.setStations(List.of("Kota", "Jaipur", "Sikar","Loharu","Churu"));
		Map<String,String> mapKotaExpress = Map.of("Kota", "10:00", "Jaipur", "11:00", "Sikar", "12:00","Loharu","13:00","Churu","14:00");
		kotaExpress.setStationTimes(mapKotaExpress);
		kotaExpress.getSeats().get(0).set(0, 1);
		kotaExpress.getSeats().get(0).set(1, 1);
		kotaExpress.getSeats().get(1).set(0, 1);
		kotaExpress.getSeats().get(1).set(1, 1);
		kotaExpress.getSeats().get(2).set(0, 1);


		User user = new User();
		user.setUserId("gautam101");

		Ticket ticket1 = new Ticket();
		ticket1.setTrain(kotaExpress);
		ticket1.setTicketId("101010");
		ticket1.setDestination("Sikar");
		ticket1.setSource("Jaipur");
		ticket1.setUserId(user.getUserId());
		ticket1.setDateOfTravel("2021-01-01");

		Ticket ticket2 = new Ticket();
		ticket2.setTrain(kotaExpress);
		ticket2.setTicketId("202020");
		ticket2.setDestination("Jaipur");
		ticket2.setSource("Kota");
		ticket2.setUserId(user.getUserId());
		ticket2.setDateOfTravel("2021-05-01");

		user.setName("Gautam");
		user.setPassword("gautam@123");
		user.setHashedPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
		user.setTicketsBooked(List.of(ticket1, ticket2));

		User user1 = new User();
		user1.setUserId("Hanu101");

		Ticket ticket11 = new Ticket();
		ticket11.setTrain(kotaExpress);
		ticket11.setTicketId("110110110");
		ticket11.setDestination("Churu");
		ticket11.setSource("Jaipur");
		ticket11.setUserId(user1.getUserId());
		ticket11.setDateOfTravel("2020-03-05");

		Ticket ticket12 = new Ticket();
		ticket12.setTrain(kotaExpress);
		ticket12.setTicketId("120120120");
		ticket12.setDestination("Loharu");
		ticket12.setSource("Jaipur");
		ticket12.setUserId(user1.getUserId());
		ticket12.setDateOfTravel("2024-03-20");

		Ticket ticket13 = new Ticket();
		ticket13.setTrain(kotaExpress);
		ticket13.setTicketId("130130130");
		ticket13.setDestination("Loharu");
		ticket13.setSource("Sikar");
		ticket13.setUserId(user1.getUserId());
		ticket13.setDateOfTravel("2024-03-20");

		user1.setName("HanuMan Singh");
		user1.setPassword("Maruti@123");
		user1.setHashedPassword(Base64.getEncoder().encodeToString(user1.getPassword().getBytes()));
		user1.setTicketsBooked(List.of(ticket11, ticket12, ticket13));

		return List.of(user,user1);
	}
	public static void createUserJSONdB(List<User> userList){
		String PATH = "src/main/java/com/maruj/ticketBooking/localDB/";
		User user;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		File file = new File(PATH+"userData.json");
		file.getParentFile().mkdirs();
		try {
			mapper.writeValue(file, userList);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
