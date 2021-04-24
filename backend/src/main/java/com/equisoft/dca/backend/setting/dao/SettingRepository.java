package com.equisoft.dca.backend.setting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equisoft.dca.backend.setting.model.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
}
