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
@NamedQueries({
		@NamedQuery(name = "sale.totalByMonth",
				query = "SELECT new br.com.rafaelvieira.shopbeer.domain.dto.SalesMonthDTO( "
						+ "FUNCTION('MONTH', s.createDate), "
						+ "SUM(s.amount)) "
						+ "FROM Sale s "
						+ "GROUP BY FUNCTION('MONTH', s.createDate)"),
		@NamedQuery(name = "sale.byOrigin",
				query = "SELECT new br.com.rafaelvieira.shopbeer.domain.dto.OriginSalesDTO( "
						+ "FUNCTION('MONTH', s.createDate), "
						+ "COUNT(s)) "
						+ "FROM Sale s "
						+ "GROUP BY FUNCTION('MONTH', s.createDate)")
})
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
	@JoinColumn(name = "code_user_employee")
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
	
	public void calculateTotalValue() {
		this.amount = calculateTotalValue(getAmountItens(), getValueShipping(), getValueDiscount());
	}
	
	public Long getCreationDays() {
		LocalDate start = createDate != null ? createDate.toLocalDate() : LocalDate.now();
		return ChronoUnit.DAYS.between(start, LocalDate.now());
	}
	
	public boolean isSaveAllowed() {
		return !status.equals(StatusSale.CANCELED);
	}
	
	public boolean isSaveForbidden() {
		return !isSaveAllowed();
	}
	
	private BigDecimal calculateTotalValue(BigDecimal totalValueItems, BigDecimal valueShipping, BigDecimal valueDiscount) {
		BigDecimal amount = totalValueItems
				.add(Optional.ofNullable(valueShipping).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(valueDiscount).orElse(BigDecimal.ZERO));
		return amount;
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
