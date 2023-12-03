package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sale")
@DynamicUpdate
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "value_shipping")
	private BigDecimal valueShipping;

	@Column(name = "value_discount")
	private BigDecimal valueDiscount;

	private BigDecimal amount = BigDecimal.ZERO;

	private String observation;

	@Column(name = "date_time_delivery")
	private LocalDateTime dateTimeDelivery;

	@ManyToOne
	@JoinColumn(name = "code_costumer")
	private Costumer costumer;

	@ManyToOne
	@JoinColumn(name = "code_user")
	private UserEmployee userEmployee;

	@Enumerated(EnumType.STRING)
	private StatusSale status = StatusSale.BUDGET;

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<ItemSale> itens = new ArrayList<>();

	@Transient
	private String uuid;

	@Transient
	private LocalDate deliveryDate;

	@Transient
	private LocalTime deliverySchedule;


	public boolean isNew() {
		return code == null;
	}
	
	public void AddItems(List<ItemSale> itens) {
		this.itens = itens;
		this.itens.forEach(i -> i.setSale(this));
	}
	
	public BigDecimal getAmountItens() {
		return getItens().stream()
				.map(ItemSale::getAmount)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void calcularValorTotal() {
		this.amount = calcularValorTotal(getAmountItens(), getValueShipping(), getValueDiscount());
	}
	
	public Long getDiasCriacao() {
		LocalDate inicio = createDate != null ? createDate.toLocalDate() : LocalDate.now();
		return ChronoUnit.DAYS.between(inicio, LocalDate.now());
	}
	
	public boolean isSalvarPermitido() {
		return !status.equals(StatusSale.CANCELED);
	}
	
	public boolean isSalvarProibido() {
		return !isSalvarPermitido();
	}
	
	private BigDecimal calcularValorTotal(BigDecimal valorTotalItens, BigDecimal valorFrete, BigDecimal valorDesconto) {
		BigDecimal valorTotal = valorTotalItens
				.add(Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO));
		return valorTotal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
