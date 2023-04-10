import cs3500.animator.controller.IAnimatorController;

class MockAnimatorController implements IAnimatorController {
  StringBuilder log;

  MockAnimatorController(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void restart() {
    log.append("restarted controller");
  }

  @Override
  public void togglePlay() {
    log.append("toggled play");
  }

  @Override
  public void toggleLoop() {
    log.append("toggled loop");
  }

  @Override
  public void changeTempo(double rate) {
    log.append("changed tempo by: ").append(rate);
  }

  @Override
  public void play() {
    log.append("played the animation");
  }

  @Override
  public double getTempo() {
    log.append("asked for tempo");
    return 0;
  }
}
