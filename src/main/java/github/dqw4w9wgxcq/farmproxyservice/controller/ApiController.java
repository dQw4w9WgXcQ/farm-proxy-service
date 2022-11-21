package github.dqw4w9wgxcq.farmproxyservice.controller;

import github.dqw4w9wgxcq.farmproxyservice.controller.dto.ProvisionDto;
import github.dqw4w9wgxcq.farmproxyservice.controller.dto.SessionDto;
import github.dqw4w9wgxcq.farmproxyservice.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiController {
    private final SessionService sessionService;

    @PostMapping("provision")
    public Map<String, SessionDto> postProvision(@RequestBody List<ProvisionDto> provisions) {
        var sessions = new HashMap<String, SessionDto>();
        for (var provision : provisions) {
            var session = sessionService.getSessionElseProvision(provision.account(), provision.geo());
            if (session != null) {
                sessions.put(provision.account(), new SessionDto(session.id(), session.geo(), session.ip()));
            }
        }

        return sessions;
    }

    //for debugging
    @GetMapping("all-sessions")
    public List<Map<String, Object>> getAllSessions() {
        return sessionService.getAllSessions().entrySet().stream().map(it -> Map.of("account", it.getKey(), "sessions", it.getValue())).collect(Collectors.toList());
    }

    @GetMapping("session")
    public ResponseEntity<SessionDto> getSession(@RequestParam String account, @RequestParam String geo) {
        var session = sessionService.getSessionElseProvision(account, geo);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        return ResponseEntity.ok(new SessionDto(session.id(), session.geo(), session.ip()));
    }

    @PostMapping("replace-session")
    public void replaceSession(@RequestParam String account, @RequestParam String banIp) {
        throw new Error("todo");//todo
    }
}
