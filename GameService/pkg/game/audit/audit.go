package audit

import (
	"context"
	"fmt"
	"time"

	"google.golang.org/grpc"
)

func AuditUnaryInterceptor(
	ctx context.Context,
	req interface{},
	info *grpc.UnaryServerInfo,
	handler grpc.UnaryHandler,
) (interface{}, error) {
	start := time.Now()

	// Call the actual handler
	resp, err := handler(ctx, req)

	// Log the audit details
	fmt.Printf("\n=== [AUDIT LOG - GameService] ===\n")
	fmt.Printf("Time       : %s\n", start.Format(time.RFC3339))
	fmt.Printf("Method     : %s\n", info.FullMethod)
	fmt.Printf("Request    : %+v\n", req)
	fmt.Printf("Response   : %+v\n", resp)
	if err != nil {
		fmt.Printf("Error      : %v\n", err)
	}
	fmt.Println("=================================\n")

	return resp, err
}
