package com.example.minions.mapper;

import com.example.minions.dto.request.AccountDTOCreate;
import com.example.minions.dto.request.AccountDTOUpdate;
import com.example.minions.dto.request.OTPRequest;
import com.example.minions.dto.response.AccountDTOResponse;
import com.example.minions.dto.response.AccountResponseAdmin;
import com.example.minions.entity.Account;
import com.example.minions.model.TokenPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountDTOCreate accountDTOCreate);

    Account toAccount(AccountDTOUpdate accountDTOUpdate);

    AccountResponseAdmin toAccountResponseAdmin(Account account);

    List<AccountResponseAdmin> toAccountResponseAdminList(List<Account> accounts);

    Account toAccount(OTPRequest otpRequest);

    AccountDTOResponse toAccountDTOResponse(Account account);

    List<AccountDTOResponse> toAccountDTOResponseList(List<Account> accounts);

    TokenPayload toTokenPayload(Account account);
}
