package org.yann.integerasiorderkuota.orderkuota.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
public class User {


	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String email;
	private String password;

	private String username;
	private String token;
	private String callbackUrl;

	@Lob
	private byte[] qrisImage;

	private String qrisString;


}
