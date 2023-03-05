package com.diden.demo.domain.main.service;

import com.diden.demo.domain.main.repository.MainContentMapper;
import com.diden.demo.domain.main.vo.request.MainContentVoRequest;
import com.diden.demo.domain.main.vo.response.MainContentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainContentServiceImpl implements MainContentService {
  private final MainContentMapper mainContentMapper;

  @Override
  public List<MainContentVo> findMainContentImageAll() {
    return mainContentMapper.findMainContentImageAll();
  }

  @Override
  public MainContentVo findMainContentImageOne(MainContentVoRequest mainContentVoRequest) {
    return null;
  }

  @Override
  public Long findMainContentImageCount() {
    return mainContentMapper.findMainContentImageCount();
  }

  @Override
  public void saveMainContentImages(MainContentVoRequest mainContentVoRequest) {
    mainContentMapper.saveMainContentImages(mainContentVoRequest);
  }
}
