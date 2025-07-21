package com.fullstack.Backend.Repository;

import com.fullstack.Backend.Model.Clicks;
import com.fullstack.Backend.Model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClicksRepository extends JpaRepository<Clicks,Long> {
    List<Clicks> findByUrl(Url url);
}
